event("Test","Annotation").

//norm(obligation,
//     evacuate(P,X,Y), 
//    "10<=X & X<=40 & 20<=Y & Y<=80", 
//     "at_loc(P,X) & unsafe(X) & safe(Y)",
//     emergency_level(X,low),
//     12).

//at_loc(annotated,15).
//unsafe(15).
//safe(30).

+emergency_level(X,high) : at_loc(P,X) & safe(Y)
    <- .puts("Emergency level in #{X} is high, evacuating");
       !evacuate(P,X,Y).

+!evacuate(P,X,Y) : at_loc(P,X)
    <- .puts("Evacuating from location #{X} to location #{Y}");
       move(X,Y).
	   
+norm(Type,Norm,Constraint,Activation,Expiration,Identifier) [source(S)]: true
	<- edu.meneguzzi.nubdi.action.AddNorm(norm(Type,Norm,Constraint,Activation,Expiration,Identifier));
	   .puts("Added norm #{Type} - #{Norm} - #{Constraint} - #{Activation} - #{Expiration} - #{Identifier}").