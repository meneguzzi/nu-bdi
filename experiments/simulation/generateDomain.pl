
makeEnvironment(MinBombX,MinBombY,MaxBombX,MaxBombY,NumBombs,
      MinUnsafeWidth,MaxUnsafeWidth,MinUnsafeHeight,MaxUnsafeHeight,
      MinUnsafeCost,MaxUnsafeCost,MinUnsafeCenterDist,MaxUnsafeCenterDist,
      NumUnsafes,BombList,UnsafeSet) :-
  placeBomb(MinBombX,MinBombY,MaxBombX,MaxBombY,NumBombs,BombList),
  makeNUnsafes(MinUnsafeWidth,MaxUnsafeWidth,MinUnsafeHeight,MaxUnsafeHeight,
        MinUnsafeCost,MaxUnsafeCost,MinUnsafeCenterDist,MaxUnsafeCenterDist,
        NumUnsafes,BombList,UnsafeList),
  setifyUnsafeList(UnsafeList,UnsafeSet).
  
makeNUnsafes(MinUnsafeWidth,MaxUnsafeWidth,MinUnsafeHeight,MaxUnsafeHeight,
        MinUnsafeCost,MaxUnsafeCost,MinUnsafeCenterDist,MaxUnsafeCenterDist,
        NumUnsafes,BombList,UnsafeList) :-
makeNUnsafes(MinUnsafeWidth,MaxUnsafeWidth,MinUnsafeHeight,MaxUnsafeHeight,
        MinUnsafeCost,MaxUnsafeCost,MinUnsafeCenterDist,MaxUnsafeCenterDist,
        NumUnsafes,BombList,[],UnsafeList).

makeNUnsafes(_,_,_,_,_,_,_,_,0,_,CurUnsafeList,CurUnsafeList).

makeNUnsafes(MinUnsafeWidth,MaxUnsafeWidth,MinUnsafeHeight,MaxUnsafeHeight,
      MinUnsafeCost,MaxUnsafeCost,MinUnsafeCenterDist,MaxUnsafeCenterDist,
      NumUnsafes,BombList,CurUnsafeList,UnsafeList) :-
  placeUnsafe(MinUnsafeWidth,MaxUnsafeWidth,MinUnsafeHeight,MaxUnsafeHeight,MinUnsafeCost,MaxUnsafeCost,MinUnsafeCenterDist,MaxUnsafeCenterDist,BombList,Unsafe),
  append(Unsafe,CurUnsafeList,TmpUnsafeList),
  NU is NumUnsafes-1,
makeNUnsafes(MinUnsafeWidth,MaxUnsafeWidth,MinUnsafeHeight,MaxUnsafeHeight,
      MinUnsafeCost,MaxUnsafeCost,MinUnsafeCenterDist,MaxUnsafeCenterDist,
      NU,BombList,TmpUnsafeList,UnsafeList).


placeBomb(MinX,MinY,MaxX,MaxY,NumBombs,Ans):-
  placeBomb(MinX,MinY,MaxX,MaxY,NumBombs,[],Ans).   

placeBomb(_,_,_,_,NumBombs,Ans,Ans):-length(Ans,NumBombs).
placeBomb(MinX,MinY,MaxX,MaxY,NumBombs,CurBombList,Ans) :-
  X is random(MaxX-MinX)+MinX,
  Y is random(MaxY-MinY)+MinY,
  not(member((X,Y),CurBombList)),
  append([(X,Y)],CurBombList,Tmp),
  placeBomb(MinX,MinY,MaxX,MaxY,NumBombs,Tmp,Ans).
placeBomb(MinX,MinY,MaxX,MaxY,NumBombs,CurBombList,Ans) :-
  placeBomb(MinX,MinY,MaxX,MaxY,NumBombs,CurBombList,Ans).   

placeUnsafe(MinWidth,MaxWidth,MinHeight,MaxHeight,MinCost,MaxCost,MinCenterDist,MaxCenterDist,BombList,UnsafeList) :-
  Width is random(MaxWidth-MinWidth)+MinWidth,
  Height is random(MaxHeight - MinHeight) + MinHeight,
  Cost is random(MaxCost - MinCost) + MinCost,
  CenterDist is random(MaxCenterDist - MinCenterDist) + MaxCenterDist,
  length(BombList,L),
  R is random(L),
  nth0(R,BombList,(X,Y)),
  randPosNeg(CenterDist,CenterDistX),
  randPosNeg(CenterDist,CenterDistY),
  Left is round(X-(Width/2)+CenterDistX),
  Bottom is round(Y-(Height/2)+CenterDistY),
  generateBombList(Left,Bottom,Width,Height,UnsafeListNoCost),
  addCostToList(UnsafeListNoCost,Cost,UnsafeList).

addCostToList(U,C,R) :- addCostToList(U,C,[],R).

addCostToList([],_,C,C).
addCostToList([(X,Y)|T],C,CurList,FinList) :-
  append([(C,X,Y)],CurList,Tmp),
  addCostToList(T,C,Tmp,FinList).

generateBombList(X0,Y0,W,H,UnsafeList) :-
  XT is X0 + W,
  YT is Y0 + H,
  generateBombList(X0,Y0,X0,Y0,XT,YT,[],UnsafeList).

generateBombList(_,_,_,YT,_,YT,CurUnsafeList,CurUnsafeList).

generateBombList(X0,Y0,XT,YC,XT,YT,CurUnsafeList,UnsafeList):-
  X is X0,
  Y is YC+1,
  generateBombList(X0,Y0,X,Y,XT,YT,CurUnsafeList,UnsafeList).

generateBombList(X0,Y0,XC,YC,XT,YT,CurUnsafeList,UnsafeList) :-
  X is XC+1,
  append([(XC,YC)],CurUnsafeList,Tmp),
  generateBombList(X0,Y0,X,YC,XT,YT,Tmp,UnsafeList).

randPosNeg(A,X) :- X is random(A).
randPosNeg(A,X) :- X is -random(A).

setifyUnsafeList(UnsafeList,UnsafeSet) :-
  setifyUnsafeList(UnsafeList,[],UnsafeSet).

setifyUnsafeList([],C,C).

setifyUnsafeList([H|T],CurSet,UnsafeSet):-
  containsGreater(H,T),
  setifyUnsafeList(T,CurSet,UnsafeSet).

setifyUnsafeList([H|T],CurSet,UnsafeSet) :-
  append([H],CurSet,Tmp),
  setifyUnsafeList(T,Tmp,UnsafeSet).
  
containsGreater(_,[]) :- fail.
containsGreater((V,X,Y),[(V2,X,Y)|_]) :- V2>V.
containsGreater(O,[_|T]) :- containsGreater(O,T).
