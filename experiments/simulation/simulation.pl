%Our basic unsafe is of the form
%unsafe(cost,x,y)
%maximum length of path we consider
maxLength(50).

moveFromTo(XC,YC,X,Y,MaxViols,UnsafeList,Path) :-
  moveTo(X,Y,[[(XC,YC,0,0,0)]],MaxViols,UnsafeList,Path).

moveTo(_,_,[],_,_,_) :- fail.

moveTo(X,Y,[P|_],_,_,P) :-
  P=[(X,Y,NumViols,Cost,ViolCost)|_],
  %reverse(P,RP),
  length(P,L),
  write(':'),write(L),write(' '),write(NumViols),write(' '),writeln(ViolCost).
  %writeln(P),writeln(' ').

%skip the move if it becomes too expensive. This will not  work, as we just end up
%readding it to the list. Actually, it should still work as we still pop the
%move off the stack. Maybe the cost is too high
%moveTo(X,Y,[[(_,_,_,Cost,_)|_]|T],MaxViols,UnsafeList,Path) :-
%  Cost>50,write('skipping'),
%  moveTo(X,Y,T,MaxViols,UnsafeList,Path).

moveTo(X,Y,[PH|PT],MaxViols,UnsafeList,Path) :- addMoveE(PH,UnsafeList,PathE),
                     addMoveW(PH,UnsafeList,PathW),
                     addMoveN(PH,UnsafeList,PathN),
                     addMoveS(PH,UnsafeList,PathS),
                     tryInsert(PathE,MaxViols,PT,Paths1),
                     tryInsert(PathW,MaxViols,Paths1,Paths2),
                     tryInsert(PathN,MaxViols,Paths2,Paths3),
                     tryInsert(PathS,MaxViols,Paths3,Paths4),!,
                     moveTo(X,Y,Paths4,MaxViols,UnsafeList,Path).

distance(X,Y,XC,YC,D) :- D is (X-XC)*(X-XC)+(Y-YC)*(Y-YC). 

addMoveE([(XC,YC,NV,CurCost,ViolCost)|PR],UnsafeList,NewPath) :- 
  XN is XC+1,
  computeNewPath(XC,YC,XN,YC,NV,CurCost,ViolCost,UnsafeList,PR,NewPath).

addMoveW([(XC,YC,NV,CurCost,ViolCost)|PR],UnsafeList,NewPath) :- 
  XN is XC-1,
  computeNewPath(XC,YC,XN,YC,NV,CurCost,ViolCost,UnsafeList,PR,NewPath).

addMoveN([(XC,YC,NV,CurCost,ViolCost)|PR],UnsafeList,NewPath) :- 
  YN is YC+1,
  computeNewPath(XC,YC,XC,YN,NV,CurCost,ViolCost,UnsafeList,PR,NewPath).

addMoveS([(XC,YC,NV,CurCost,ViolCost)|PR],UnsafeList,NewPath) :- 
  YN is YC-1,
  computeNewPath(XC,YC,XC,YN,NV,CurCost,ViolCost,UnsafeList,PR,NewPath).

%compute new path, the two options identify whether it is a violation or not
computeNewPath(XC,YC,XN,YN,NV,CurCost,ViolCost,UnsafeList,PR,NewPath) :-
  member((C,XN,YN),UnsafeList),
  NNV is NV+1,
  distance(XN,YN,XC,YC,Dist),
  length(PR,Cost),
  TotalCost is ViolCost+Cost+C+Dist,
  NewViolCost is ViolCost+C,
  append([(XN,YN,NNV,TotalCost,NewViolCost)],[(XC,YC,NV,CurCost,ViolCost)|PR],NewPath).

computeNewPath(XC,YC,XN,YN,NV,CurCost,ViolCost,_,PR,NewPath) :-
  distance(XN,YN,XC,YC,Dist),
  length(PR,Cost),
  TotalCost is ViolCost+Cost+Dist,
  append([(XN,YN,NV,TotalCost,ViolCost)],[(XC,YC,NV,CurCost,ViolCost)|PR],NewPath).

%insert the path if we can, otherwise skip the insertion
tryInsert(Path,MaxViols,PathList,NewPathList) :-
  notTooLong(Path), %a better optimisation might be max dist from destination
  notTooManyViols(MaxViols,Path),
  doesNotContain(Path,PathList),
  insert(Path,PathList,[],NewPathList).

tryInsert(_,_,PathList,PathList).

%check if the path is not too long
notTooLong(Path) :-length(Path,X),maxLength(Y),X=<Y.

%check if the number of violations is less than the max permitted
notTooManyViols(MaxViols,[(_,_,NV,_,_)|_]) :- NV=<MaxViols.
%check that we have not visited this spot before

doesNotContain(_,[]).
doesNotContain([(X,Y,_,_,_)|T],[PH|PT]) :-
 notContainSinglePath(X,Y,T), 
 notContainSinglePath(X,Y,PH),
 doesNotContain([(X,Y,_,_,_)],PT).

notContainSinglePath(_,_,[]).
notContainSinglePath(X,Y,[(XN,YN,_,_,_)|T]):-
  not(distance(X,Y,XN,YN,0)),
  notContainSinglePath(X,Y,T).

insert(Path,[],CurrentPaths,NP):-append(CurrentPaths,[Path],NP).

insert(Path,[PathsHead|PathsTail],CurrentPaths,NewPaths) :-
  insertBefore(PathsHead,Path),
  append(CurrentPaths,[Path,PathsHead|PathsTail],NewPaths).

insert(Path,[PathsHead|PathsTail],CurrentPaths,NewPaths) :-
  append(CurrentPaths,[PathsHead],Tmp),
  insert(Path,PathsTail,Tmp,NewPaths).

insertBefore([(_,_,_,PC,_)|_],[(_,_,_,CPC,_)|_]) :- CPC<PC.
