lexicon Exp.Syntax.Lex;

lexicon "lexicon"

semi ";"

dot "."

lCurly "{"

rCurly "}"

minus "-"

comma ","

up "^"

colon ":"

char "'" ( "\\" [trn\'] | [^\] ) "'"

char {
	S { 
		"'" : Open; 
	}
	Open { 
		"\\"  : Ecran;
		^"\\" : Ready:
	}
	Ecran {
		"trn\\'" : Ready;
	}
	Ready {
		"'" : Close;
	}		
	final Close
}

string {
	S { 
		"\"" : Open; 
	}
	Open { 
		"\\" : Ecran;
		"\""  : Close;
		^"\\\"" : Open;
	}
	Ecran {
		"trn\\\"" : Open;
	}
	final Close
}

upperIdent {
	S {
		'A'-'Z' : F;
	}
	final F {
		'A'-'Z' : F;
		'a'-'z' : F;
		'0'-'9' : F;
	}
}

lowerIdent {
	S {
		'a'-'z' : F;
	}
	final F {
		'A'-'Z' : F;
		'a'-'z' : F;
		'0'-'9' : F;
	}
}