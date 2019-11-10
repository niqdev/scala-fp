package com.github

import com.github.niqdev.ShowSyntax

package object niqdev {

  object all extends AllSyntax
  object show extends ShowSyntax
}

trait AllSyntax extends ShowSyntax
