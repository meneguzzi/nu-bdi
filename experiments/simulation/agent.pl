:- include('simulation.pl').
:- include('generateDomain.pl').

%parameters for makeEnvironment:
%MinBombX,MinBombY,MaxBombX,MaxBombY,NumBombs,
%MinUnsafeWidth,MaxUnsafeWidth,MinUnsafeHeight,MaxUnsafeHeight,
%MinUnsafeCost,MaxUnsafeCost,MinUnsafeCenterDist,MaxUnsafeCenterDist,
%NumUnsafes
%Max values are actual Max specified - 1
% These generate a BombList (containing positions of bombs) and UnsafeList
% (containing positions of Unsafes)
% We then itterate between minviol and maxviol-1 to see if we can do better
% as we are allowed to violate more.
% For testing: 
%  makeEnvironment(2,2,5,5,2,
%                  1,3,1,4,
%                  1,3,0,4,
%                  1,BombList,UnsafeList),

doFullRun(MinViol,MaxViol):-
  makeEnvironment(1,1,20,20,10,
                  1,5,1,5,
                  1,4,0,6,
                  8,BombList,UnsafeList),
  doFullRun(MinViol,MaxViol,MinViol,BombList,UnsafeList).

doFullRun(_,MaxViol,MaxViol,_,_).
doFullRun(MinViol,MaxViol,CurViol,BombList,UnsafeList):-
  run(0,0,CurViol,BombList,UnsafeList,Utility),
  write(CurViol),write(':'),writeln(Utility),
  T is CurViol+1,
doFullRun(MinViol,MaxViol,T,BombList,UnsafeList).

run(SX,SY,MaxViols,BombList,UnsafeList,Utility) :-
  append([(SX,SY)],BombList,MoveList),
  doMoves(MoveList,MaxViols,UnsafeList,Utility).

doMoves(BL,MaxViols,UnsafeList,U):-doMoves(BL,MaxViols,UnsafeList,0,U).

doMoves([_],_,_,Utility,Utility).
doMoves([(XC,YC)|[(X,Y)|T]],MaxViols,UnsafeList,CurUtility,Utility) :-
write('trying to move from '),write(XC),write(','),write(YC),
write(' to '),write(X),write(','),write(Y),
 moveFromTo(XC,YC,X,Y,MaxViols,UnsafeList,Path),
 Path=[(X,Y,_,Cost,_)|_], 
 TmpUtility is CurUtility+Cost,
 doMoves([(X,Y)|T],MaxViols,UnsafeList,TmpUtility,Utility).


%Note: we try move from XC YC to the new dest as X,Y could not be reached
doMoves([(XC,YC)|[(_,_)|T]],MaxViols,UnsafeList,CurUtility,Utility) :-
 writeln('failed to achieve a goal'),
 %Path=[(X,Y,_,Cost,_)|_], 
 TmpUtility is CurUtility, 
 doMoves([(XC,YC)|T],MaxViols,UnsafeList,TmpUtility,Utility).
