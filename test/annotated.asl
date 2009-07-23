event("Test","Annotation").

norm(obligation,
     evacuate(P,X,Y), 
     "10<=X & X<=40 & 20<=Y & Y<=80", 
     "at_loc(P,X) & unsafe(X) & safe(Y)",
     emergency_level(X,low),
     12).
event(a(B),c(DS)).

@plan[constraint(X>10)]
+event(X,Y) : true
	<- action(X) [constraint(X>10)];
	   //.println(X) [Y];
	   .puts("Annotation worked").
	   
+norm(Type,Norm,Constraint,Activation,Expiration,Identifier) [source(S)]: true
	<- edu.meneguzzi.nubdi.action.AddNorm(norm(Type,Norm,Constraint,Activation,Expiration,Identifier));
	   .puts("Added norm #{Type} - #{Norm} - #{Constraint} - #{Activation} - #{Expiration} - #{Identifier}").