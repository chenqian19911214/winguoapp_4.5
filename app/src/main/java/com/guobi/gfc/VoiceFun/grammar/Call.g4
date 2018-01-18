grammar Call;

	s	:	u d n '给' x 	
		|	u d '给' x
		|	u '给' x d n
		|	j x
		|	u h x l ;

	u	:	a? ;
	d	:	'打' | '拨' | '拨打' ;
	n	:	'电话' | '号' ; 
	j	:	'接' | '接通' | '拨' | '拨打' | '联系' ;
	h	:	'和' | '对' | '跟' ;
	l	:	'通话' | '对话' | '说话' | '联系' ;
	x	:	a ;
	
	a	:	(A .*?) ;
	A	:	.+? ;
	
