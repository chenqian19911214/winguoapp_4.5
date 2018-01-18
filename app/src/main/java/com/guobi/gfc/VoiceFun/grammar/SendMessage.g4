grammar SendMessage;

		s	:	u '告诉' y C z
			|	u '告诉' w
	
			|	u f M '给' y C z
			|	u f M '给' w
		
			|	u f x '给' y C z
			|	u f x '给' w
	
			|	u '给' y v M C z
			|	u '给' y v M x
	
			|	u '给' y f x C z
			|	u '给' y f x 
		
			|	u b M v '给' y C z
			|	u b M v '给' w
	
			|	u b x v '给' y C z
			|	u b x v '给' w
	
			|	u M v '给' y C z
			|	u M v '给' w
	
			|	x v '给' y C z
			|	x v '给' w ;


			u	:	a? ;
			w	:	a ;
			f   :	'发' | '发送' | '分享' ;
			x	:	a ;
			y	:	a ;
			z	:	a? ;
			b	:	'把' | '将' ; 
			v	:	f? ;  

			M	:	G | L G ; 
			L	:	D | N D ;
	
fragment	D	:	'条' | '个' ;
fragment	N	:	'一' ;
fragment	G	:	'短信' | '信息' | '消息' | '信' ;


			C   :	J H K | '告诉'H | '告诉'H K | K | '内容是' ;
	
fragment	J   :	'和' | '对' | '跟' ;
fragment	K	:	'说' | '讲' ;
fragment	H	:	'他' | '她' | '它' | '他们' | '它们' | '她们' ;

	
			a	:	(A .*?) ;
			A	:	.+? ;
	
	
