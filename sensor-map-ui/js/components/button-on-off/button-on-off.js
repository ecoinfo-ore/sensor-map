 class ButtonOnOff extends HTMLElement {

   constructor() { 
      super()    ;
      this.attachShadow({ 'mode': 'open' }) ;
      this._id    = "coby-on-off-button-id"      ;
      this._title = "coby-button-on-off-title"   ;
      this.event  = "coby-eventType-BuilderMode" ;
   }

   connectedCallback() { 

     if( this.hasAttribute('_id') ) {
	this._id = this.getAttribute('_id');
     }

     if( this.hasAttribute('_title') ) {
	this._title = this.getAttribute('_title');
     }
   
     if( this.hasAttribute('event') ) {
	this.event = this.getAttribute('event');
     }

     this.shadowRoot.innerHTML =  `
            
          <link href="js/components/button-on-off/css/style.css" rel="stylesheet" />

          <div class="switch demo">
  	    <input type="checkbox" id="${this._id}" ><label><i></i></label>
	  </div>

     ` ;

     this.shadowRoot
         .getElementById( this._id )
         .onclick = _ => {
               var checked = this.shadowRoot.getElementById( this._id ).checked; 
                window.dispatchEvent( new CustomEvent ( this.event , 
                                         {  bubbles: true    , 
                                            composed: true   ,
                                            detail: {
						message: checked						
					    } 
                                         } 
                               ) ) ;
         } ;
   }
 }

 customElements.define("button-on-off", ButtonOnOff ) ;

 
