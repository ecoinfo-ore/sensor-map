
import  '../xml/js/lib/jquery-2.1.3.min.js' 
import  "../xml/js/lib/jxon/index.js"
import  "../xml/js/lib/jqContextMenu/src/jquery.contextMenu.js"
import  "../xml/js/lib/jqTree/tree.jquery.js"
import  "../xml/js/lib/vkbeautify.js"
import { init } from  "../xml/js/xml-edit.js"
import  "../xml/js/lib/ace-builds-master/src-min-noconflict/ace.js"


export default  class XmlEditor  extends HTMLElement {

   constructor() { 
     super()     ;
    // this.attachShadow({ 'mode': 'open' } ) ; 
   }

   connectedCallback() { 

     ace.config.set('basePath', '/js/components/xml/js/lib/ace-builds-master/src-min-noconflict/')    
       
       // this.shadowRoot.innerHTML = 
        
       document.getElementById("xml-editor").innerHTML =  `
      
            <head>
                <title>Easy XML-Editor</title>
                
                <link rel="stylesheet" href="js/components/xml/css/xml-editor.css">
                <link rel="stylesheet" href="js/components/xml/js/lib/jqTree/jqtree.css">
                <!-- <link rel="stylesheet" href="js/components/xml/js/lib/bootstrap/css/bootstrap.min.css"> -->
                
                <link href="js/components/xml/js/lib/jqContextMenu/src/jquery.contextMenu.css" rel="stylesheet" type="text/css"/>
                
            </head>
            
            <body>
                <div id="menu">
                <a role="button" id="save-button" href="#">
                    <span aria-hidden="true" class="glyphicon glyphicon-save"></span> Save
                </a>
                <input type="file" id="save" role="button" nwsaveas style="display: none;"/>
                <button role="button" class="print">
                    <span aria-hidden="true" class="glyphicon glyphicon-picture"></span> Print
                </button>
                </div>
                
                <div id="tabcontainer">
                <div class="tab-buttons">
                    <div role="button" id="btn-tree" data-tab="xml-tree" class="selected">
                    <span>XML-Tree</span>
                    </div>
                    <div role="button" id="btn-text" data-tab="xml-text">
                    <span>XML-Editor</span>
                    </div>
                </div>
                <div id="tabs">
                    <div id="xml-tree" style="display: block;"></div>
                    <div id="xml-text" style="display: none;"></div>
                </div>
                 <script type="text/javascript">
                 $(function () {
                     init();
                 });
                 </script>
                </div>
                
                <div id="modal-editattribut" class="xml-modal" style="display: none;">
                <h3 class="xml-modal-title">Edit attribute</h3>
                <div class="xml-modal-body">
                    <div class="line">
                    <label class="pos-left">Name <input type="text" id="attr-name"></label>
                    <label class="pos-right">Value <input type="text" id="attr-value"></label>
                    </div>
                    <div class="line">
                    <button id="ch-attr-cancel" role="button" class="pos-left">Cancel</button>
                    <button id="ch-attr-ok" role="button" class="pos-right">O.K.</button>
                    </div>
                </div>
                </div>
                <div id="print-section">
                <canvas id="print-canvas" class="print-canvas"></canvas>
                <canvas id="buf-canvas"></canvas>
                </div>
                <div id="imgview" style="display: none">
                <button role="button" id="saveimg">
                    <span class="glyphicon glyphicon-download-alt"></span> Save
                </button>
                <input type="file" id="imgsave" role="button" nwsaveas style="display: none;"/>
                <img id="image">
                </div>
            </body>
       
     `

     $(function () {
         init()    ;
     } ) ;
     
   }  

 }
 
 customElements.define("xml-editor", XmlEditor ) ;

