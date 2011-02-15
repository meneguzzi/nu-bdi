+goTo(X) : true
	<- !goTo(X).

+!goTo(C) : true
    <- !getVehicle(car);
       !moveTo(C).

+!getVehicle(V) : true
    <- +hasVehicle(V).

+!moveTo(C) : ~at(C)
	<- +at(C).

+norm(Type,Norm,Constraint,Activation,Expiration,Identifier) [source(S)]: true
	<- edu.meneguzzi.nubdi.action.AddNorm(norm(Type,Norm,Constraint,Activation,Expiration,Identifier));
	   .puts("Added norm #{Type} - #{Norm} - #{Constraint} - #{Activation} - #{Expiration} - #{Identifier}").