package com.github.niqdev.free

sealed trait Condition[T]
case class Equal[T](actual: T, expected: T)   extends Condition[T]
case class StartsWith[T](value: T, prefix: T) extends Condition[T]
case class EndsWith[T](value: T, suffix: T)   extends Condition[T]
case class Match[T](value: T, regex: String)  extends Condition[T]
case class In[T](value: T, values: List[T])   extends Condition[T]

sealed trait Interpreter[T] {
  def eval(condition: Condition[T]): Boolean
}
object Interpreter {
  def apply[T](implicit ev: Interpreter[T]): Interpreter[T] = ev

  implicit val stringInterpreter: Interpreter[String] =
    new Interpreter[String] {
      override def eval(condition: Condition[String]): Boolean =
        condition match {
          case Equal(actual: String, expected: String) => actual == expected
          case _                                       => false
        }
    }

  implicit val intInterpreter: Interpreter[Int] =
    new Interpreter[Int] {
      override def eval(condition: Condition[Int]): Boolean =
        condition match {
          case Equal(actual: Int, expected: Int) => actual == expected
          case _                                 => false
        }
    }
}

object ConditionMain extends App {

  // TODO combine FreeB[Condition[String]] and FreeB[Condition[Int]]
  val conditions: FreeB[Condition[String]] =
    FreeB.Pure[Condition[String]](Equal("aaa", "bbb")) ||
      FreeB.Pure[Condition[String]](Equal("", ""))

  println(conditions.run(Interpreter[String].eval))
  println(FreeB.Pure[Condition[Int]](Equal(1, 1)).run(Interpreter[Int].eval))
}
