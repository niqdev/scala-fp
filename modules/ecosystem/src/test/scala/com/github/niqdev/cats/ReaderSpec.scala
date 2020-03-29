package com.github.niqdev.cats

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

final class ReaderSpec extends AnyWordSpecLike with Matchers {

  "Reader" should {

    "verify composition" in {
      import cats.data.Reader
      import cats.syntax.applicative.catsSyntaxApplicativeId

      final case class Db(users: Map[Int, String], passwords: Map[String, String])

      type DbReader[T] = Reader[Db, T]

      def findUser(userId: Int): DbReader[Option[String]] =
        Reader(db => db.users.get(userId))

      def checkPassword(user: String, password: String): DbReader[Boolean] =
        Reader(db => db.passwords.get(user).contains(password))

      def checkLogin(userId: Int, password: String): DbReader[Boolean] =
        for {
          maybeUser <- findUser(userId)
          isValid <- maybeUser
            .map(user => checkPassword(user, password))
            .getOrElse(false.pure[DbReader]) // applicative
        } yield isValid

      val users     = Map(1 -> "user1", 2 -> "user2", 3 -> "user3")
      val passwords = Map("user1" -> "password1", "user2" -> "password2", "user3" -> "password3")
      val db        = Db(users, passwords)

      checkLogin(1, "password1").run(db) shouldBe true
      checkLogin(8, "password1").run(db) shouldBe false
      checkLogin(1, "invalid").run(db) shouldBe false
    }
  }

}
