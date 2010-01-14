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

//First case is when we arrive at the location, then we start executing the moves
+!moveTo(X,Y, [m(X,Y,D) | MT]) : true
	<- .puts("Current position is #{X}, #{Y}, executing moves.");
	   !executeMoves([m(X,Y,D) | MT]).
	
+!moveTo(X,Y, [m(XC,YC,D) | MT]) : XC < X
	<- .puts("Current position is #{XC}, #{YC}, going to #{X}, #{Y}, moving to e.");
	   !moveTo(X,Y, [ m(XC+1, YC, e) | [m(XC,YC,D) | MT ]]).

+!moveTo(X,Y, [m(XC,YC,D) | MT]) : XC > X
	<- .puts("Current position is #{XC}, #{YC}, going to #{X}, #{Y}, moving to w.");
	   !moveTo(X,Y, [ m(XC-1, YC, w) | [m(XC,YC,D) | MT ]]).

+!moveTo(X,Y, [m(XC,YC,D) | MT]) : YC < Y
	<- .puts("Current position is #{XC}, #{YC}, going to #{X}, #{Y}, moving to n.");
	   !moveTo(X,Y, [ m(XC, YC+1, n) | [m(XC,YC,D) | MT ]]).

+!moveTo(X,Y, [m(XC,YC,D) | MT]) : YC > Y
	<- .puts("Current position is #{XC}, #{YC}, going to #{X}, #{Y}, moving to s.");
	   !moveTo(X,Y, [ m(XC, YC-1, s) | [m(XC,YC,D) | MT ]]).
	
//Finished moving
+!executeMoves([]) : true
	<- .puts("Arrived at location").
	
+!executeMoves([m(_,_,_) | MT]) : true
	<- .puts("Moving to direction #{D}");
	   !executeMoves([MT]).
	   
+!executeMoves([m(_,_,e) | MT]) : true
	<- .puts("Moving to direction #{D}");
	   !executeMoves([MT]).

+!executeMoves([m(_,_,w) | MT]) : true
	<- .puts("Moving to direction #{D}");
	   !executeMoves([MT]).

+!executeMoves([m(_,_,n) | MT]) : true
	<- .puts("Moving to direction #{D}");
	   !executeMoves([MT]).
	   
+!executeMoves([m(_,_,s) | MT]) : true
	<- .puts("Moving to direction #{D}");
	   !executeMoves([MT]).

//this is the entry point, startMoving(0,0,3,3) routes from 3,3 to 0,0
+!startMoving(X,Y,XS,YS): true 
	<- !moveTo(X,Y,[m(XS,YS,x)]).

+move(X,Y) : true
	<- !startMoving(X,Y,0,0).
	   
+norm(Type,Norm,Constraint,Activation,Expiration,Identifier) [source(S)]: true
	<- edu.meneguzzi.nubdi.action.AddNorm(norm(Type,Norm,Constraint,Activation,Expiration,Identifier));
	   .puts("Added norm #{Type} - #{Norm} - #{Constraint} - #{Activation} - #{Expiration} - #{Identifier}").