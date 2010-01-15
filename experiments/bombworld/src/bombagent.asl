//AGENT NAME is an

!clearBomb.

+!clearBomb:not bomb(_,_,_) <- true.

+!clearBomb : bomb(X,Y,T) & bin(XB,YB,T) 
   <- !moveTo(X,Y);
      pickup;
      !moveTo(XB,YB);
      drop;
      !clearBomb.

+!moveTo(X,Y) : agent(bombagent,XS,YS)
  <- !moveTo(X,Y,[[m(XS,YS,x)]]).

+!moveTo(_,_,[]) 
  <- .puts("failed") ; .fail.

+!moveTo(X,Y,[[m(X,Y,D)|P]|_]) : true
  <- .concat([m(X,Y,D)],P,Path);
     .reverse(Path,RP);
     RP=[HR|TR];
     .print(TR);
     !doMove(TR);
//     .print("cleared cache");
     !clearCache.

+!moveTo(X,Y,[[m(XC,YC,_)|_]|MLT]): unsafe(XC,YC)
  <- !moveTo(X,Y,MLT).

//alreadyMovedTo for the first list
//+!moveTo(X,Y,[[m(XC,YC,_)|L]|MLT]): .member(m(XC,YC,_),L)
//  <- !moveTo(X,Y,MLT).

//+!moveTo(X,Y,[[m(XC,YC,_)|_]|MLT])
//  <- !alreadyVisited(m(XC,YC),MLT);
//     !moveTo(X,Y,MLT).

//added to do with triedVisit
+!moveTo(X,Y,[[m(XC,YC,_)|_]|MLT]): triedVisit(XC,YC)
  <- !moveTo(X,Y,MLT).

//-!moveTo(X,Y,[[m(XC,YC,_)|L]|MLT]) <- !moveTo(X,Y,[[m(XC,YC,_)|_]|MLT]).


+!moveTo(X,Y,[ML|MT])
  <- //.print(X); .print(Y); .print(ML); .print(MT);
     !addMoveE(ML,NMLE);
     !addMoveW(ML,NMLW);
     !addMoveN(ML,NMLN);
     !addMoveS(ML,NMLS);
     .concat([NMLE],[NMLW],NMLEW);
     .concat([NMLN],NMLEW,NMLNEW);
     .concat([NMLS],NMLNEW,NMLSNEW);
     .concat(MT,NMLSNEW,MTN);
     //.print(MTN);
     !moveTo(X,Y,MTN).

+!addMoveE([m(XC,YC,D)|T],R) 
  <- XN = XC+1 ;
     .concat([m(XN,YC,e)],[m(XC,YC,D)|T],R) ; 
     +triedVisit(XC,YC).
+!addMoveW([m(XC,YC,D)|T],R) <- XN = XC-1; .concat([m(XN,YC,w)],[m(XC,YC,D)|T],R).
+!addMoveN([m(XC,YC,D)|T],R) <- YN = YC+1; .concat([m(XC,YN,n)],[m(XC,YC,D)|T],R).
+!addMoveS([m(XC,YC,D)|T],R) <- YN = YC-1; .concat([m(XC,YN,s)],[m(XC,YC,D)|T],R).

+!doMove([]).
+!doMove([m(_,_,D)|T]) 
  <-  move(D);//.print(T);
      !doMove(T).

+!clearCache : triedVisit(X,Y) <- -triedVisit(X,Y); !clearCache.
+!clearCache.
