// Устарело... больше нет upper и lowerIdent-ов
lexicon Exp.Syntax.Lex;

syntax "syntax"

semi ";"

dot "."

star "*"

colon ":"

uses "uses"

lexicon "lexicon"

eq "="

upperIdent fsm{
	S {
		'A'-'Z' -> F;
	}
	final F {
		'A'-'Z','a'-'z','0'-'9' -> F
	}
}

lowerIdent fsm{
	S {
		'a'-'z' -> F;
	}
	final F {
		'A'-'Z','a'-'z','0'-'9' -> F
	}
}