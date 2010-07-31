@testPlan1
+event(A,B) : true
   <- action(A,B).

@testPlan2
+event(A,B) : true
   <- action(A,B);
      action2(A,B).

@testPlan3
+event(A,B) : true
   <- action(A,B);
      action2(A,B);
      action2(A,B).

@testPlan4
+norm(Type,Norm,Constraint,Activation,Expiration,Identifier) [source(S)]: true
	<- edu.meneguzzi.nubdi.action.AddNorm(norm(Type,Norm,Constraint,Activation,Expiration,Identifier));
	   .puts("Added norm #{Type} - #{Norm} - #{Constraint} - #{Activation} - #{Expiration} - #{Identifier}").