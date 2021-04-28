// TODO notes
// https://www.youtube.com/watch?v=7xSfLPD6tiQ
// https://www.youtube.com/watch?v=XZ9nPZbaYfE
// https://free.cofree.io/2017/11/13/recursion

// recursion schema
// * generalize recursion from values to types
// * it allows to "factor recursion out" of the semantics of your program

// fix point combinator
// https://en.wikipedia.org/wiki/Fixed-point_combinator

val factorial: Int => Int = n => if (n == 0) 1 else n * factorial(n - 1)
factorial(8)

// a fix point is a function that given a function f, fix(f) == f(fix(f))
def fix[A](f: (=> A) => A): A = { lazy val a: A = f(a); a }
// no longer recursive but still stack overflow
val factorialF = fix[Int => Int](f => n => if (n == 0) 1 else n * f.apply(n - 1))

factorialF(8)

// fix point type
// https://en.wikipedia.org/wiki/Fixed_point_(mathematics)

case class Wagon0(id: Int, wagons: List[Wagon0])
// not recursive
case class Wagon[T](id: Int, wagons: List[T])

// same with types
case class Fix[F[_]](unfix: F[Fix[F]])
type WagonF = Fix[Wagon]

val wagons: WagonF = Fix(Wagon(1, List(Fix(Wagon(2, List.empty)))))

/*
Recursion Schemes:
- Anamorphism (generalize unfolds)
- Catamorphism (generalized folds)
- Paramorphism (more powerful generalized fold)
- Apomorphism (more powerful generalized unfold)
- Histomorphism (yet more powerful generalized unfold): Cofree
- Futumorphism (yet more powerful generalized fold): Free
*/

trait M[A] {
  def flatMap[B](f: A => M[B]): M[B]
}
def unit[A](x: A): M[A]

trait Comonad[F[_]] {
  def extract[A](fa: F[A]): A
  def coflatMap[A, B](fa: F[A])(f: F[A] => B): F[B]
}

/*
NonEmptyList
`extract` == Nel.head
`coflatMap` == coflatMap(NonEmptyList(1,2,3))(nel => nel.head + 1) == NonEmptyList(2, 3, 4)
*/

// >>> TODO
// http://blog.higher-order.com/blog/2015/06/23/a-scala-comonad-tutorial

// Pure Functional Database Programming with Fixpoint Types—Rob Norris
// https://www.youtube.com/watch?v=7xSfLPD6tiQ

// recursive data type
case class ProfF[A](
  name: String,
  year: Int,
  students: List[A]
)

case class Prof0(value: ProfF[Prof0])
case class IdProf0(id: Int, value: ProfF[IdProf0])

case class Prof1[F[_]](value: F[Prof1[F]])
case class IdProf1[F[_], A](id: A, value: F[IdProf1[F, A]])

// >>> category theory
// >>> inductive data type
// ProfF is a traversable functor: we can sequence (flip type constructor)
// TODO traversable functor

// a fix point for any constructor of type F
// a way to represent an infinite type in a finite way
case class Fix[F[_]](unfix: F[Fix[F]])
// same but it gives a structure with an annotation associated to each value
// A and F
case class Cofree[F[_], A](head: A, tail: F[Cofree[F, A]])
// Free monad
// a structure with a label at the leaves (?)
//case class Free[F[_], A](resume: A \/ F[Free[F, A]])

// you can always extract an F from Fix and Cofree
// if you have an F you can always construct Fix and Free: called recursive type
// corecursive in the other case

type Prof = Fix[Prof]
type IdProf = Cofree[IdProf, Int]
