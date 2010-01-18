//AGENT NAME is an

!clearBomb.

+!clearBomb:not bomb(_,_,_) 
   <- .puts("All bombs cleared. Yay!");
      endSimulation;
      .puts("Finished Simulation").

+!clearBomb : bomb(X,Y,T) & bin(XB,YB,T) 
   <- !moveTo(X,Y); .puts("Picking up bomb #{T} from #{X},#{Y}");
      pickup;
      !moveTo(XB,YB);
      .puts("Dropping bomb #{T} at #{XB},#{YB}");
      //!moveTo(X,Y); 
      drop;
      !clearBomb.

+!moveTo(X,Y) : agent(bombagent,XS,YS) 
  <- .puts("Planning movement from #{XS},#{YS} to #{X},#{Y}.");
     !moveTo(X,Y,[[m(1,XS,YS,x)]]).

+!moveTo(_,_,[]) <- .puts("Failed to find a path, sorry") ; .fail.

+!moveTo(X,Y,[[m(Dist,X,Y,D)|P]|_]) : true
  <- .concat([m(Dist,X,Y,D)],P,Path);
     .reverse(Path,RP);
     RP=[HR|TR];
     !doMove(TR);
     !clearCache.

//-----DEAL WITH SPECIAL CASE WHERE WE START AT UNSAFE. We
//assume we can get out of there with one move.
+!moveTo(X,Y,[[m(1,XC,YC,x)]]): unsafe(XC,YC)
  <- 
     !addMoveE([m(1,XC,YC,x)],NMLE);
     !addMoveW([m(1,XC,YC,x)],NMLW);
     !addMoveN([m(1,XC,YC,x)],NMLN);
     !addMoveS([m(1,XC,YC,x)],NMLS);
     !insert(NMLE,[],MT1);
     !insert(NMLW,MT1,MT2);
     !insert(NMLN,MT2,MT3);
     !insert(NMLS,MT3,MT4);
     !moveTo(X,Y,MT4).

+!addMoveE([m(Dist,XC,YC,D)],R) 
  <- XN = XC+1 ;
     DistNew=1+(XN-X)*(XN-X)+(YC-Y)*(YC-Y);
     .concat([m(DistNew,XN,YC,e)],[m(Dist,XC,YC,D)],R) ; 
     +triedVisit(XC,YC). //we only need to add it once

+!addMoveW([m(Dist,XC,YC,D)],R) 
  <- XN = XC-1;
     DistNew=1+(XN-X)*(XN-X)+(YC-Y)*(YC-Y);
     .concat([m(DistNew,XN,YC,w)],[m(Dist,XC,YC,D)],R).

+!addMoveN([m(Dist,XC,YC,D)],R) 
  <- YN = YC+1; 
     DistNew=1+(XC-X)*(XC-X)+(YN-Y)*(YN-Y);
     .concat([m(DistNew,XC,YN,n)],[m(Dist,XC,YC,D)],R).

+!addMoveS([m(Dist,XC,YC,D)],R) 
  <- YN = YC-1; 
     DistNew=1+(XC-X)*(XC-X)+(YN-Y)*(YN-Y);
     .concat([m(DistNew,XC,YN,s)],[m(Dist,XC,YC,D)],R).
//-----END DEAL WITH SPECIAL CASE WHERE WE START AT UNSAFE

+!moveTo(X,Y,[[m(Dist,XC,YC,_)|_]|MLT]): unsafe(XC,YC) 
<- !moveTo(X,Y,MLT).

//added to do with triedVisit
+!moveTo(X,Y,[[m(Dist,XC,YC,_)|_]|MLT]): triedVisit(XC,YC)
  <- !moveTo(X,Y,MLT).

+!moveTo(X,Y,[ML|MT])
  <- !addMoveE(ML,NMLE);
     !addMoveW(ML,NMLW);
     !addMoveN(ML,NMLN);
     !addMoveS(ML,NMLS);
     !insert(NMLE,MT,MT1);
     !insert(NMLW,MT1,MT2);
     !insert(NMLN,MT2,MT3);
     !insert(NMLS,MT3,MT4);
     !moveTo(X,Y,MT4).

//Debug code to print the first element of a list
//+!printHead([])<-.print(" fin head ").
//+!printHead([[m(Dist,X,Y,D)|T]|T2]) <- A=m(Dist,X,Y,D) ; .print(A) ;
//!printHead(T2).

+!addMoveE([m(Dist,XC,YC,D)|T],R) 
  <- XN = XC+1 ;
     DistNew=.length(T)+2+(XN-X)*(XN-X)+(YC-Y)*(YC-Y);
     .concat([m(DistNew,XN,YC,e)],[m(Dist,XC,YC,D)|T],R) ; 
     +triedVisit(XC,YC). //we only need to add it once

+!addMoveW([m(Dist,XC,YC,D)|T],R) 
  <- XN = XC-1;
     DistNew=.length(T)+2+(XN-X)*(XN-X)+(YC-Y)*(YC-Y);
     .concat([m(DistNew,XN,YC,w)],[m(Dist,XC,YC,D)|T],R).

+!addMoveN([m(Dist,XC,YC,D)|T],R) 
  <- YN = YC+1; 
     DistNew=.length(T)+2+(XC-X)*(XC-X)+(YN-Y)*(YN-Y);
     .concat([m(DistNew,XC,YN,n)],[m(Dist,XC,YC,D)|T],R).

+!addMoveS([m(Dist,XC,YC,D)|T],R) 
  <- YN = YC-1; 
     DistNew=.length(T)+2+(XC-X)*(XC-X)+(YN-Y)*(YN-Y);
     .concat([m(DistNew,XC,YN,s)],[m(Dist,XC,YC,D)|T],R).

//+!doMove([]).
//+!doMove([m(_,_,_,D)|T]) 
//  <-  move(D);
//      !doMove(T).

//alternate doMove which checks for unsafe
+!doMove([]).
//+!doMove([m(_X,Y,D)|T]): unsafe(X,Y)
//  <- !clearCache ; !moveTo(X,Y).  //replan the move

//compute the destination we're trying to move to, once we find it, replan the
//move.
+!moveToDestination([m(_,X,Y,D)]) <- !moveTo(X,Y). 
+!moveToDestination([_|T]) <-
 !moveToDestination(T).

-!doMove([m(_,X,Y,D)|T]) : agent(bombagent,XS,YS) //true
  <- .puts("Found an unsafe square at #{X},#{Y} moving #{D}, replanning.");
     !clearCache;
     +unsafe(X,Y);
     !moveToDestination([m(_X,Y,D)|T]).

@planMove
+!doMove([m(_,X,Y,D)|T])//: not unsafe(X,Y)
  <- .puts("Moving #{D} to #{X},#{Y}.");
     move(D);
     !doMove(T).

+!clearCache : triedVisit(X,Y) <- -triedVisit(X,Y); !clearCache.
+!clearCache.

+!insert(New,[],[New]).
+!insert(New,List,Ret) <- !insert(New,List,[],Ret).

+!insert([m(Dist,XC,YC,D)|T],[],RC,Ret)<-.concat(RC,[[m(Dist,XC,YC,D)|T]],Ret).

+!insert([m(Dist,XC,YC,D)|T],[[m(Distp,XCP,YCP,DP)|RT]|RPT],RC,Ret) : Dist<Distp
   <- .concat(RC,[[m(Dist,XC,YC,D)|T]],RH);
     .concat(RH,[[m(Distp,XCP,YCP,DP)|RT]],RHN);
     .concat(RHN,RPT,Ret).

+!insert([m(Dist,XC,YC,D)|T],[[m(Distp,XCP,YCP,DP)|RT]|RPT],RC,Ret) : Dist>=Distp  
  <- .concat(RC,[[m(Distp,XCP,YCP,DP)|RT]],RCN);
     !insert([m(Dist,XC,YC,D)|T],RPT,RCN,Ret).

+norm(Type,Norm,Constraint,Activation,Expiration,Identifier) [source(S)]: true
  <- .abolish(unsafe(_,_));
     edu.meneguzzi.nubdi.action.AddNorm(norm(Type,Norm,Constraint,Activation,Expiration,Identifier));
     .puts("Added norm #{Type} - #{Norm} - #{Constraint} - #{Activation} - #{Expiration} - #{Identifier}").