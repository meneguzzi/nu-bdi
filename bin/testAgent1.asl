@testPlan1
+event(A,B) : true
   <- action(A,B).

@testPlan2
+emergency_level(X,high) : at_loc(P,X) & safe(Y)
    <- .puts("Emergency level in #{X} is high, evacuating");
       !evacuate(P,X,Y).

@testPlan3
+!evacuate(P,X,Y) : at_loc(P,X)
    <- .puts("Evacuating from location #{X} to location #{Y}");
       move(X,Y).

@testPlan4	   
+norm(Type,Norm,Constraint,Activation,Expiration,Identifier) [source(S)]: true
	<- edu.meneguzzi.nubdi.action.AddNorm(norm(Type,Norm,Constraint,Activation,Expiration,Identifier));
	   .puts("Added norm #{Type} - #{Norm} - #{Constraint} - #{Activation} - #{Expiration} - #{Identifier}").