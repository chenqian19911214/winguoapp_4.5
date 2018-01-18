grammar Launch;

	s	:	j x;

	j	:	'打开' | '启动' | '运行';
	x	:	a ;
	
	a	:	(A .*?) ;
	A	:	.+? ;
	