package com.github.niqdev.cats

import cats.Monad
import org.scalatest.{Matchers, WordSpecLike}

final class MonadSpec extends WordSpecLike with Matchers {

  "Monad" should {

    "verify option" in {
      import cats.instances.option.catsStdInstancesForOption

      Monad[Option].pure(3) shouldBe Some(3)
      Monad[Option].flatMap(Option(3))(value => Some(value * 3)) shouldBe Some(9)
      Monad[Option].map(Option(3))(_ * 3) shouldBe Some(9)
    }

    "verify sumSquare" in {
      import cats.instances.option.catsStdInstancesForOption
      import cats.syntax.applicative.catsSyntaxApplicativeId
      import cats.syntax.flatMap.toFlatMapOps
      import cats.syntax.functor.toFunctorOps

      def sumSquare[F[_] : Monad](a: F[Int], b: F[Int]): F[Int] =
        a.flatMap(a0 => b.map(b0 => a0 * a0 + b0 * b0))

      // context bound
      def sumSquareFor[F[_] : Monad](a: F[Int], b: F[Int]): F[Int] =
        for {
          a0 <- a // flatMap
          b0 <- b // map
        } yield a0 * a0 + b0 * b0

      val expected = Some(13)
      sumSquare(Option(2), Option(3)) shouldBe expected
      sumSquareFor[Option](2.pure[Option], 3.pure[Option]) shouldBe expected
    }

    "verify Id" in {
      import cats.Id
      import cats.syntax.applicative.catsSyntaxApplicativeId
      import cats.syntax.flatMap.toFlatMapOps
      import cats.syntax.functor.toFunctorOps

      Monad[Id].pure(42) shouldBe 42
      Monad[Id].flatMap(42)(_ / 2) shouldBe 21

      val sum = for {
        a <- 2.pure[Id]
        b <- 3.pure[Id]
        c <- 4.pure[Id]
      } yield a + b + c

      sum shouldBe 9
    }

    // fail-fast error handling
    "verify Either" in {
      import cats.syntax.either.{catsSyntaxEither, catsSyntaxEitherId, catsSyntaxEitherObject}

      3.asRight[String] shouldBe Right(3)
      "error".asLeft[Int] shouldBe Left("error")

      Either.catchOnly[NumberFormatException]("error".toInt)
        .leftMap(_.getMessage) shouldBe Left("For input string: \"error\"")
      Either.catchNonFatal(sys.error("error"))
        .leftMap(_.getMessage) shouldBe Left("error")
      Either.fromTry(scala.util.Try("error".toInt))
        .leftMap(_.getMessage) shouldBe Left("For input string: \"error\"")
      Either.fromOption[String, Int](None, "error") shouldBe Left("error")

      "error".asLeft[Int].leftMap(_.reverse) shouldBe Left("rorre")
      6.asRight[String].bimap(_.reverse, _ * 7) shouldBe Right(42)
      "error".asLeft[Int].bimap(_.reverse, _ * 7) shouldBe Left("rorre")
    }

    "verify MonadError" in {
      import cats.instances.try_.catsStdInstancesForTry
      import cats.syntax.applicativeError.catsSyntaxApplicativeErrorId

      val exception: Throwable = new RuntimeException("error")
      exception.raiseError[scala.util.Try, Int] shouldBe scala.util.Failure(exception)
    }

    "verify Eval" in {
      import cats.Eval

      // val is eager and memoized
      Eval.now(math.random + 1000).value should be >= 0.0
      // def is lazy and not memoized
      Eval.always(math.random + 3000).value should be >= 0.0
      // lazy val is lazy and memoized
      Eval.later(math.random + 2000).value should be >= 0.0

      def factorial(n: BigInt): Eval[BigInt] =
        if (n == 1) {
          Eval.now(n)
        } else {
          Eval.defer(factorial(n - 1).map(_ * n))
        }

      factorial(1000).value shouldBe BigInt("402387260077093773543702433923003985719374864210714632543799910429938512398629020592044208486969404800479988610197196058631666872994808558901323829669944590997424504087073759918823627727188732519779505950995276120874975462497043601418278094646496291056393887437886487337119181045825783647849977012476632889835955735432513185323958463075557409114262417474349347553428646576611667797396668820291207379143853719588249808126867838374559731746136085379534524221586593201928090878297308431392844403281231558611036976801357304216168747609675871348312025478589320767169132448426236131412508780208000261683151027341827977704784635868170164365024153691398281264810213092761244896359928705114964975419909342221566832572080821333186116811553615836546984046708975602900950537616475847728421889679646244945160765353408198901385442487984959953319101723355556602139450399736280750137837615307127761926849034352625200015888535147331611702103968175921510907788019393178114194545257223865541461062892187960223838971476088506276862967146674697562911234082439208160153780889893964518263243671616762179168909779911903754031274622289988005195444414282012187361745992642956581746628302955570299024324153181617210465832036786906117260158783520751516284225540265170483304226143974286933061690897968482590125458327168226458066526769958652682272807075781391858178889652208164348344825993266043367660176999612831860788386150279465955131156552036093988180612138558600301435694527224206344631797460594682573103790084024432438465657245014402821885252470935190620929023136493273497565513958720559654228749774011413346962715422845862377387538230483865688976461927383814900140767310446640259899490222221765904339901886018566526485061799702356193897017860040811889729918311021171229845901641921068884387121855646124960798722908519296819372388642614839657382291123125024186649353143970137428531926649875337218940694281434118520158014123344828015051399694290153483077644569099073152433278288269864602789864321139083506217095002597389863554277196742822248757586765752344220207573630569498825087968928162753848863396909959826280956121450994871701244516461260379029309120889086942028510640182154399457156805941872748998094254742173582401063677404595741785160829230135358081840096996372524230560855903700624271243416909004153690105933983835777939410970027753472000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")
    }

    "verify Writer" in {
      import cats.data.Writer
      import cats.instances.vector.catsKernelStdMonoidForVector
      import cats.syntax.applicative.catsSyntaxApplicativeId
      import cats.syntax.writer.catsSyntaxWriterId

      val log = Vector("msg1", "msg2", "msg3")
      val result = 123

      val writer = Writer(log, result)
      writer.written shouldBe log
      writer.value shouldBe result
      writer.run shouldBe Tuple2(log, result)

      /*
       * The log in a Writer is preserved when we map or flatMap over it.
       * flatMap appends the logs from the source Writer and the result of the user’s sequencing function.
       * For this reason it's good practice to use a log type that has an
       * efficient append and concatenate operations, such as a Vector
       */
      type Logged[A] = Writer[Vector[String], A]

      // requires semigroup
      val writerFor = for {
        a <- 2.pure[Logged] // only result, no log
        _ <- Vector("add-2").tell // append only log, no result
        b <- 3.pure[Logged]
        _ <- Vector("add-3").tell
        c <- 4.writer(Vector("add-4")) // result and log
        _ <- Vector("end").tell
      } yield a + b + c // result

      writerFor.written shouldBe Vector("add-2", "add-3", "add-4", "end")
      writerFor.value shouldBe 9
      writerFor.run shouldBe Tuple2(Vector("add-2", "add-3", "add-4", "end"), 9)
    }

    "verify Reader" in {
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
            .getOrElse(false.pure[DbReader])
        } yield isValid

      val users = Map(1 -> "user1", 2 -> "user2", 3 -> "user3")
      val passwords = Map("user1" -> "password1", "user2" -> "password2", "user3" -> "password3")
      val db = Db(users, passwords)

      checkLogin(1, "password1").run(db) shouldBe true
      checkLogin(8, "password1").run(db) shouldBe false
      checkLogin(1, "invalid").run(db) shouldBe false
    }
  }

}
