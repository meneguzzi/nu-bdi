//AGENT NAME is an

!clearBomb.

+!clearBomb:not bomb(_,_,_) <- true.

+!clearBomb : bomb(X,Y,T) & bin(XB,YB,T) 
   <- !moveTo(X,Y);
      pickup;
      !moveTo(XB,YB);
      drop;
      !clearBomb.

+!moveTo(X,Y) : agent(bombagent,XS,YS) <- !moveTo(X,Y,[[m(1,XS,YS,x)]]).

+!moveTo(_,_,[]) <- .puts("failed") ; .fail.

+!moveTo(X,Y,[[m(Dist,X,Y,D)|P]|_]) : true
  <- .concat([m(Dist,X,Y,D)],P,Path);
     .reverse(Path,RP);
     RP=[HR|TR];
     !doMove(TR);
     !clearCache.

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

+!doMove([]).
+!doMove([m(_,_,_,D)|T]) 
  <-  move(D);
      !doMove(T).

//alternate doMove which checks for unsafe
//+!doMove([m(_X,Y,D)|T]): unsafe(X,Y)
//  <- +!clearCache ; !moveTo(X,Y).  //replan the move
//+!doMove([m(_,X,Y,D)|T]):// not unsafe(X,Y)
//  <- move(D);
//     !doMove(T).

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
  <- .concat([[m(Distp,XCP,YCP,DP)|RT]],RC,RCN);
     !insert([m(Dist,XC,YC,D)|T],RPT,RCN,Ret).
