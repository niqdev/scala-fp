package com.github.niqdev

final case class Kleisli[F[_], A, B](run: A => F[B]) {
  self =>

  def compose[C](k: Kleisli[F, C, A])(implicit F: Monad[F]): Kleisli[F, C, B] =
    Kleisli[F, C, B] { c: C =>
      val fa: F[A] = k.run(c)
      F.flatMap(fa) { a: A =>
        val fb: F[B] = self.run(a)
        fb
      }
    }

  def andThen[C](f: B => F[C])(implicit F: Monad[F]): Kleisli[F, A, C] =
    Kleisli[F, A, C] { a: A =>
      val fb: F[B] = self.run(a)
      F.flatMap(fb) { b: B =>
        val fc: F[C] = f(b)
        fc
      }
    }

  def andThen[C](k: Kleisli[F, B, C])(implicit F: Monad[F]): Kleisli[F, A, C] =
    self.andThen(k.run)

  def flatMap[C](f: B => Kleisli[F, A, C])(implicit F: Monad[F]): Kleisli[F, A, C] =
    Kleisli[F, A, C] { a: A =>
      val fb: F[B] = self.run(a)
      F.flatMap(fb) { b: B =>
        val k: Kleisli[F, A, C] = f(b)
        val fc: F[C]            = k.run(a)
        fc
      }
    }

  def map[C](f: B => C)(implicit F: Functor[F]): Kleisli[F, A, C] =
    Kleisli[F, A, C] { a: A =>
      val fb: F[B] = self.run(a)
      F.map(fb) { b: B =>
        val c: C = f(b)
        c
      }
    }
}
