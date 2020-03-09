
function hashData( data, algo ) {

    if (algo === 'MD5')  {
        return MD5(data) ;
    }

    if (algo === 'SHA1')  {
        return SHA1(data) ;
    }

    if (algo === 'SHA2')    {
        return SHA256(data) ;
    }
    return data ;
}

function hash(  login              ,
                password           ,
                algoSigneture      ,
                loginSignature     ,
                passwordSignature  ,
                timeStampSignature )                          {

    var timestamp  = Math.round(+new Date() / 1000)           ;

    var _login     = hashData(login, loginSignature)          ;
    var _password  = hashData(password, passwordSignature)    ;
    var _timestamp = hashData(timestamp, timeStampSignature)  ;

    var Plaintoken = _login.concat(_password, _timestamp)     ;

    var sign       = hashData(Plaintoken, algoSigneture)      ;
    checkAuth ( login, timestamp, sign )                      ;
}

function SHA256(ascii) {
    
    function rightRotate(value, amount) {
        return (value >>> amount) | (value << (32 - amount)) ;
    } ;

    var mathPow = Math.pow        ;
    var maxWord = mathPow(2, 32)  ;
    var lengthProperty = 'length' ;
    var i, j                      ; // Used as a counter across the whole file
    var result = ''               ;

    var words = []                                 ;
    var asciiBitLength = ascii[lengthProperty] * 8 ;

    //* caching results is optional - remove/add slash from front of this line to toggle
    // Initial hash value: first 32 bits of the fractional parts of the square roots of the first 8 primes
    // (we actually calculate the first 64, but extra values are just ignored)
    var hash = SHA256.h = SHA256.h || [] ;
    // Round constants: first 32 bits of the fractional parts of the cube roots of the first 64 primes
    var k = SHA256.k = SHA256.k || []    ;
    var primeCounter = k[lengthProperty] ;
    /*/
     var hash = [], k = [];
     var primeCounter = 0;
     //*/

    var isComposite = {};
    for (var candidate = 2; primeCounter < 64; candidate++) {
        if (!isComposite[candidate]) {
            for (i = 0; i < 313; i += candidate) {
                isComposite[i] = candidate       ;
            }
            hash[primeCounter] = (mathPow(candidate, .5) * maxWord)   | 0 ;
            k[primeCounter++] = (mathPow(candidate, 1 / 3) * maxWord) | 0 ;
        }
    }

    ascii += '\x80' // Append Ƈ' bit (plus zero padding)
    while (ascii[lengthProperty] % 64 - 56 )
        ascii += '\x00' // More zero padding
    for (i = 0; i < ascii[lengthProperty]; i++ ) {
        j = ascii.charCodeAt(i)  ;
        if (j >> 8 )
            return ; // ASCII check: only accept characters in range 0-255
        words[i >> 2] |= j << ((3 - i) % 4) * 8 ;
    }
    words[words[lengthProperty]] = ((asciiBitLength / maxWord) | 0);
    words[words[lengthProperty]] = (asciiBitLength)

    // process each chunk
    for (j = 0; j < words[lengthProperty]; ) {
        var w = words.slice(j, j += 16); // The message is expanded into 64 words as part of the iteration
        var oldHash = hash;
        // This is now the undefinedworking hash", often labelled as variables a...g
        // (we have to truncate as well, otherwise extra entries at the end accumulate
        hash = hash.slice(0, 8);

        for (i = 0; i < 64; i++) {
            var i2 = i + j ;
            // Expand the message into 64 words
            // Used below if 
            var w15 = w[i - 15], w2 = w[i - 2] ;

            // Iterate
            var a = hash[0], e = hash[4] ;
            var temp1 = hash[7]
                    + (rightRotate(e, 6) ^ rightRotate(e, 11) ^ rightRotate(e, 25)) // S1
                    + ((e & hash[5]) ^ ((~e) & hash[6])) // ch
                    + k[i]
                    // Expand the message schedule if needed
                    + (w[i] = (i < 16) ? w[i] : (
                            w[i - 16]
                            + (rightRotate(w15, 7) ^ rightRotate(w15, 18) ^ (w15 >>> 3)) // s0
                            + w[i - 7]
                            + (rightRotate(w2, 17) ^ rightRotate(w2, 19) ^ (w2 >>> 10)) // s1
                            ) | 0
                            );
            // This is only used once, so *could* be moved below, but it only saves 4 bytes and makes things unreadble
            var temp2 = (rightRotate(a, 2) ^ rightRotate(a, 13) ^ rightRotate(a, 22)) // S0
                    + ((a & hash[1]) ^ (a & hash[2]) ^ (hash[1] & hash[2])) ; // maj

            hash = [(temp1 + temp2) | 0].concat(hash) ;
            hash[4] = (hash[4] + temp1) | 0 ;
        }

        for (i = 0; i < 8; i++) {
            hash[i] = (hash[i] + oldHash[i]) | 0 ;
        }
    }

    for (i = 0; i < 8; i++)     {
        for (j = 3; j + 1; j--) {
            var b = (hash[i] >> (j * 8)) & 255 ;
            result += ((b < 16) ? 0 : '') + b.toString(16) ;
        }
    }
    return result ;
}


function SHA1(str) {

    function hex(value) {
        var s = (value >>> 0).toString(16)
        while (s.length < 8)
            s = '0' + s
        return s
    }

    function limit(n) {
        return n & 0xffffffff
    }

    function rotateLeft(n, s) {
        return (n << s) | (n >>> (32 - s))
    }

    var H0 = 0x67452301
    var H1 = 0xefcdab89
    var H2 = 0x98badcfe
    var H3 = 0x10325476
    var H4 = 0xc3d2e1f0

    var i, j
    var blockstart
    var W = new Array(80)
    var A, B, C, D, E
    var temp

    str = unescape(encodeURIComponent(str))
    str = str.split(/.*?/).map(function (s) {
        return s.charCodeAt(0)
    })

    var length = str.length

    var words = []
    for (i = 0; i < length - 3; i += 4) {
        j = str[i] << 24 | str[i + 1] << 16 | str[i + 2] << 8 | str[i + 3]
        words.push(j)
    }

    switch (length % 4) {
        case 0:
            i = 0x80000000
            break
        case 1:
            i = str[length - 1] << 24 | 0x800000
            break
        case 2:
            i = str[length - 2] << 24 | str[length - 1] << 16 | 0x8000
            break
        case 3:
            i = str[length - 3] << 24 | str[length - 2] << 16 | str[length - 1] << 8 | 0x80
            break
    }

    words.push(i)

    while ((words.length % 16) != 14) {
        words.push(0)
    }

    words.push(length >>> 29)
    words.push(limit(length << 3))

    for (blockstart = 0; blockstart < words.length; blockstart += 16) {

        for (i = 0; i < 16; i++) {
            W[i] = words[blockstart + i]
        }
        for (i = 16; i < 80; i++) {
            W[i] = rotateLeft(W[i - 3] ^ W[i - 8] ^ W[i - 14] ^ W[i - 16], 1)
        }

        A = H0
        B = H1
        C = H2
        D = H3
        E = H4

        for (i = 0; i <= 19; i++) {
            temp = limit(rotateLeft(A, 5) + ((B & C) | (~B & D)) + E + W[i] + 0x5a827999)
            E = D
            D = C
            C = rotateLeft(B, 30)
            B = A
            A = temp
        }

        for (i = 20; i <= 39; i++) {
            temp = limit(rotateLeft(A, 5) + (B ^ C ^ D) + E + W[i] + 0x6ed9eba1)
            E = D
            D = C
            C = rotateLeft(B, 30)
            B = A
            A = temp
        }

        for (i = 40; i <= 59; i++) {
            temp = limit(rotateLeft(A, 5) + ((B & C) | (B & D) | (C & D)) + E + W[i] + 0x8f1bbcdc)
            E = D
            D = C
            C = rotateLeft(B, 30)
            B = A
            A = temp
        }

        for (i = 60; i <= 79; i++) {
            temp = limit(rotateLeft(A, 5) + (B ^ C ^ D) + E + W[i] + 0xca62c1d6)
            E = D
            D = C
            C = rotateLeft(B, 30)
            B = A
            A = temp
        }

        H0 = limit(H0 + A)
        H1 = limit(H1 + B)
        H2 = limit(H2 + C)
        H3 = limit(H3 + D)
        H4 = limit(H4 + E)

    }

    return hex(H0) + hex(H1) + hex(H2) + hex(H3) + hex(H4)

}


/*
 * A JavaScript implementation of the RSA Data Security, Inc. MD5 Message
 * Digest Algorithm, as defined in RFC 1321.
 * Copyright (C) Paul Johnston 1999 - 2000.
 * Updated by Greg Holt 2000 - 2001.
 * See http://pajhome.org.uk/site/legal.html for details.
 */

/*
 * Convert a 32-bit number to a hex string with ls-byte first
 */

var hex_chr = "0123456789abcdef" ;

function rhex(num) {
    
    str = "";
    for (j = 0; j <= 3; j++)
        str += hex_chr.charAt((num >> (j * 8 + 4)) & 0x0F) +
                hex_chr.charAt((num >> (j * 8)) & 0x0F);
    return str;
}

/*
 * Convert a string to a sequence of 16-word blocks, stored as an array.
 * Append padding bits and the length, as described in the MD5 standard.
 */

function str2blks_MD5(str) {
    
    nblk = ((str.length + 8) >> 6) + 1 ;
    blks = new Array(nblk * 16)        ;
    for (i = 0; i < nblk * 16; i++)
        blks[i] = 0 ;
    for (i = 0; i < str.length; i++)
        blks[i >> 2] |= str.charCodeAt(i) << ((i % 4) * 8) ;
    blks[i >> 2] |= 0x80 << ((i % 4) * 8) ;
    blks[nblk * 16 - 2] = str.length * 8  ;
    return blks ;
}

/*
 * Add integers, wrapping at 2^32. This uses 16-bit operations internally 
 * to work around bugs in some JS interpreters.
 */

function add(x, y) {
    
    var lsw = (x & 0xFFFF) + (y & 0xFFFF)         ;
    var msw = (x >> 16) + (y >> 16) + (lsw >> 16) ;
    return (msw << 16) | (lsw & 0xFFFF)           ;
}

/*
 * Bitwise rotate a 32-bit number to the left
 */
function rol(num, cnt) {
    
    return (num << cnt) | (num >>> (32 - cnt)) ;
}

/*
 * These functions implement the basic operation for each round of the
 * algorithm.
 */
function cmn(q, a, b, x, s, t )
{
    return add(rol(add(add(a, q), add(x, t)), s), b);
}
function ff(a, b, c, d, x, s, t)
{
    return cmn((b & c) | ((~b) & d), a, b, x, s, t);
}
function gg(a, b, c, d, x, s, t)
{
    return cmn((b & d) | (c & (~d)), a, b, x, s, t);
}
function hh(a, b, c, d, x, s, t)
{
    return cmn(b ^ c ^ d, a, b, x, s, t);
}
function ii(a, b, c, d, x, s, t)
{
    return cmn(c ^ (b | (~d)), a, b, x, s, t);
}

/*
 * Take a string and return the hex representation of its MD5.
 */

function MD5(str)
{
    x = str2blks_MD5(str) ;
    a = 1732584193        ;
    b = -271733879        ;
    c = -1732584194       ;
    d = 271733878         ;

    for (i = 0; i < x.length; i += 16 ) {
        
        olda = a ;
        oldb = b ;
        oldc = c ;
        oldd = d ;

        a = ff(a, b, c, d, x[i + 0], 7, -680876936)   ;
        d = ff(d, a, b, c, x[i + 1], 12, -389564586)  ;
        c = ff(c, d, a, b, x[i + 2], 17, 606105819)   ;
        b = ff(b, c, d, a, x[i + 3], 22, -1044525330) ;
        a = ff(a, b, c, d, x[i + 4], 7, -176418897)   ;
        d = ff(d, a, b, c, x[i + 5], 12, 1200080426)  ;
        c = ff(c, d, a, b, x[i + 6], 17, -1473231341) ;
        b = ff(b, c, d, a, x[i + 7], 22, -45705983)   ;
        a = ff(a, b, c, d, x[i + 8], 7, 1770035416)   ;
        d = ff(d, a, b, c, x[i + 9], 12, -1958414417) ;
        c = ff(c, d, a, b, x[i + 10], 17, -42063)     ;
        b = ff(b, c, d, a, x[i + 11], 22, -1990404162);
        a = ff(a, b, c, d, x[i + 12], 7, 1804603682)  ;
        d = ff(d, a, b, c, x[i + 13], 12, -40341101)  ;
        c = ff(c, d, a, b, x[i + 14], 17, -1502002290);
        b = ff(b, c, d, a, x[i + 15], 22, 1236535329) ;

        a = gg(a, b, c, d, x[i + 1], 5, -165796510)   ;
        d = gg(d, a, b, c, x[i + 6], 9, -1069501632)  ;
        c = gg(c, d, a, b, x[i + 11], 14, 643717713)  ;
        b = gg(b, c, d, a, x[i + 0], 20, -373897302)  ;
        a = gg(a, b, c, d, x[i + 5], 5, -701558691)   ;
        d = gg(d, a, b, c, x[i + 10], 9, 38016083)    ;
        c = gg(c, d, a, b, x[i + 15], 14, -660478335) ;
        b = gg(b, c, d, a, x[i + 4], 20, -405537848)  ;
        a = gg(a, b, c, d, x[i + 9], 5, 568446438)    ;
        d = gg(d, a, b, c, x[i + 14], 9, -1019803690) ;
        c = gg(c, d, a, b, x[i + 3], 14, -187363961)  ;
        b = gg(b, c, d, a, x[i + 8], 20, 1163531501)  ;
        a = gg(a, b, c, d, x[i + 13], 5, -1444681467) ;
        d = gg(d, a, b, c, x[i + 2], 9, -51403784)    ;
        c = gg(c, d, a, b, x[i + 7], 14, 1735328473)  ;
        b = gg(b, c, d, a, x[i + 12], 20, -1926607734);

        a = hh(a, b, c, d, x[i + 5], 4, -378558)      ;
        d = hh(d, a, b, c, x[i + 8], 11, -2022574463) ;
        c = hh(c, d, a, b, x[i + 11], 16, 1839030562) ;
        b = hh(b, c, d, a, x[i + 14], 23, -35309556)  ;
        a = hh(a, b, c, d, x[i + 1], 4, -1530992060)  ;
        d = hh(d, a, b, c, x[i + 4], 11, 1272893353)  ;
        c = hh(c, d, a, b, x[i + 7], 16, -155497632)  ;
        b = hh(b, c, d, a, x[i + 10], 23, -1094730640);
        a = hh(a, b, c, d, x[i + 13], 4, 681279174)   ;
        d = hh(d, a, b, c, x[i + 0], 11, -358537222)  ;
        c = hh(c, d, a, b, x[i + 3], 16, -722521979)  ;
        b = hh(b, c, d, a, x[i + 6], 23, 76029189)    ;
        a = hh(a, b, c, d, x[i + 9], 4, -640364487)   ;
        d = hh(d, a, b, c, x[i + 12], 11, -421815835) ;
        c = hh(c, d, a, b, x[i + 15], 16, 530742520)  ;
        b = hh(b, c, d, a, x[i + 2], 23, -995338651)  ;

        a = ii(a, b, c, d, x[i + 0], 6, -198630844)   ;
        d = ii(d, a, b, c, x[i + 7], 10, 1126891415)  ;
        c = ii(c, d, a, b, x[i + 14], 15, -1416354905);
        b = ii(b, c, d, a, x[i + 5], 21, -57434055)   ;
        a = ii(a, b, c, d, x[i + 12], 6, 1700485571)  ;
        d = ii(d, a, b, c, x[i + 3], 10, -1894986606) ;
        c = ii(c, d, a, b, x[i + 10], 15, -1051523)   ;
        b = ii(b, c, d, a, x[i + 1], 21, -2054922799) ;
        a = ii(a, b, c, d, x[i + 8], 6, 1873313359)   ;
        d = ii(d, a, b, c, x[i + 15], 10, -30611744)  ;
        c = ii(c, d, a, b, x[i + 6], 15, -1560198380) ;
        b = ii(b, c, d, a, x[i + 13], 21, 1309151649) ;
        a = ii(a, b, c, d, x[i + 4], 6, -145523070)   ;
        d = ii(d, a, b, c, x[i + 11], 10, -1120210379);
        c = ii(c, d, a, b, x[i + 2], 15, 718787259)   ;
        b = ii(b, c, d, a, x[i + 9], 21, -343485551)  ;

        a = add(a, olda) ;
        b = add(b, oldb) ;
        c = add(c, oldc) ;
        d = add(d, oldd) ;
    }
    return rhex(a) + rhex(b) + rhex(c) + rhex(d) ;
}


function copyText(id) {
   try {
         copyStringToClipboard (document.getElementById(id).value) ;
       } catch (err) {
          console.log('Oops, unable to copy') ;
       }
}

function copy(_id_1, _id_2, _id_3 ) {

    processTab( _id_1 ) ;
    processTab( _id_2 ) ;
    processTab( _id_3 ) ;
    
}

function processTab( idTab ) {
    
    if( document.getElementById(idTab) === null ) return        ;
    id = idTab.split(':')[0].replace("input_", "") + "_pane"    ;
    var ElementCssClass = document.getElementById(id).className ;
    
    if ( ElementCssClass.indexOf("tab-pane") !== -1 && 
         ElementCssClass.indexOf("active") !== -1)   {
         copyText( idTab ) ;
    }
}


function copyStringToClipboard (str) {
   // Create new element
   var el = document.createElement('textarea') ;
   // Set value (string to be copied)
   el.value = str ;
   // Set non-editable to avoid focus and move outside of view
   el.setAttribute('readonly', '') ;
   el.style = {position: 'absolute', left: '-9999px'} ;
   document.body.appendChild(el) ;
   // Select text inside element
   el.select() ;
   // Copy text to clipboard
   document.execCommand('copy') ;
   // Remove temporary element
   document.body.removeChild(el);
}


function timeOut (id) {
    setTimeout(function() { $('#'+id).delay(200)
                                     .fadeOut( 2500 , 
                                        function() { $(this).html(''); } 
                                     ) ;
                          }, 900) ;
}
    
function sso( login     ,
              password  ,
              clientId  ,
              secretId  ) {

    checkAuth ( login, password, clientId, secretId ) ;
}


function login( login , password  ) {

    var timestamp  = Math.round(+new Date() / 1000)                                    ;
    var hashedLoginPassword    = hashData( login.concat(password, timestamp) , 'SHA2') ;
    
    loginChecker ( hashedLoginPassword , timestamp  )                                  ;
    
}

// $(function() {
//     
//         // greeb : rgb(0, 128, 0)
//         // white : rgb(255, 255, 255)
//         $('.changeColor').click(function() {
//             var reponse = $(this).attr('id').split('_')[1] ;
//             
//             var color = $( this ).css( "backgroundColor" ) ;
//             if( color === 'rgb(255, 255, 255)')            {
//                 $(this).css('backgroundColor', 'green')    ;
//                 try {
//                 sendMyResponse( reponse, "YES")            ;
//                 }catch( e) {
//                   console.error(e);
//                 }
//             } else if ( color === 'rgb(0, 128, 0)')        {
//                 
//                $(this).css('backgroundColor', 'white')     ;
//                 
//                try {
//                       sendMyResponse( reponse, "NO")       ;
//               } catch(e) {
//                   console.error(e) ;
//               }
//             }
//            
//         } ) ;
//} ) ;


function openPage(link)                   {
   var win = window.open(link, '_blank')  ;
   win.focus()                            ;
} 

function openOnSamePage( idTab, fileName )  {
  
    if( document.getElementById(idTab) === null ) return        ;
    id = idTab.split(':')[0].replace("input_", "") + "_pane"    ;
    var ElementCssClass = document.getElementById(id).className ;
    
    if ( ElementCssClass.indexOf("tab-pane") !== -1 && 
         ElementCssClass.indexOf("active") !== -1)   {
     
         download( document.getElementById( idTab).value ,
                                            fileName     , 
                                            "text/plain" ) ;
    }
} 

//download.js v3.0, by dandavis; 2008-2014. [CCBY2] see http://danml.com/download.html for tests/usage
// v1 landed a FF+Chrome compat way of downloading strings to local un-named files, upgraded to use a hidden frame and optional mime
// v2 added named files via a[download], msSaveBlob, IE (10+) support, and window.URL support for larger+faster saves than dataURLs
// v3 added dataURL and Blob Input, bind-toggle arity, and legacy dataURL fallback was improved with force-download mime and base64 support

// data can be a string, Blob, File, or dataURL

						 
function download(data, strFileName, strMimeType) {
	
	var self = window, // this script is only for browsers anyway...
		u = "application/octet-stream", // this default mime also triggers iframe downloads
		m = strMimeType || u, 
		x = data,
		D = document,
		a = D.createElement("a"),
		z = function(a){return String(a);},
		
		
		B = self.Blob || self.MozBlob || self.WebKitBlob || z,
		BB = self.MSBlobBuilder || self.WebKitBlobBuilder || self.BlobBuilder,
		fn = strFileName || "download",
		blob, 
		b,
		ua,
		fr;

	if(String(this)==="true"){ //reverse arguments, allowing download.bind(true, "text/xml", "export.xml") to act as a callback
		x=[x, m] ;
		m=x[0]   ;
		x=x[1]   ; 
	}
	
	//go ahead and download dataURLs right away
	if(String(x).match(/^data\:[\w+\-]+\/[\w+\-]+[,;]/)){
		return navigator.msSaveBlob ?  // IE10 can't do a[download], only Blobs:
			navigator.msSaveBlob(d2b(x), fn) : 
			saver(x) ; // everyone else can save dataURLs un-processed
	}//end if dataURL passed?
	
	try{
	
		blob = x instanceof B ? 
			x : 
			new B([x], {type: m}) ;
	}catch(y){
		if(BB){
			b = new BB();
			b.append([x]);
			blob = b.getBlob(m); // the blob
		}
		
	}
	
	function d2b(u) {
		var p= u.split(/[:;,]/),
		t= p[1],
		dec= p[2] == "base64" ? atob : decodeURIComponent,
		bin= dec(p.pop()),
		mx= bin.length,
		i= 0,
		uia= new Uint8Array(mx);

		for(i;i<mx;++i) uia[i]= bin.charCodeAt(i);

		return new B([uia], {type: t});
	 }
	  
	function saver(url, winMode) {
		
		if ('download' in a) { //html5 A[download] 			
			a.href = url;
			a.setAttribute("download", fn) ;
			a.innerHTML = "downloading..." ;
			D.body.appendChild(a) ;
			setTimeout(function() {
				a.click() ;
				D.body.removeChild(a) ;
				if(winMode===true){setTimeout(function(){ self.URL.revokeObjectURL(a.href);}, 250 ) ; }
			}, 66);
			return true;
		}
		
		//do iframe dataURL download (old ch+FF):
		var f = D.createElement("iframe") ;
		D.body.appendChild(f) ;
		if(!winMode){ // force a mime that will download:
			url="data:"+url.replace(/^data:([\w\/\-\+]+)/, u) ;
		}
		 
	
		f.src = url;
		setTimeout(function(){ D.body.removeChild(f); }, 333);
		
	}//end saver 
		
	if (navigator.msSaveBlob) { // IE10+ : (has Blob, but not a[download] or URL)
		return navigator.msSaveBlob(blob, fn);
	} 	
	
	if(self.URL) { // simple fast and modern way using Blob and URL:
		saver(self.URL.createObjectURL(blob), true) ;
	}else {
		// handle non-Blob()+non-URL browsers:
		if(typeof blob === "string" || blob.constructor===z ) {
			try {
			      return saver( "data:" +  m   + ";base64,"  +  self.btoa(blob) ) ; 
			}catch(y){
			      return saver( "data:" +  m   + "," + encodeURIComponent(blob) ) ; 
			}
		}
		
		// Blob but not URL:
		fr=new FileReader()    ;
                
		fr.onload=function(e)  {
		    saver(this.result) ; 
		} ;
		
                fr.readAsDataURL(blob) ;
	}	
	return true;
} /* end download() */

