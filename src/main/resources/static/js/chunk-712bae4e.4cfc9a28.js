(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-712bae4e"],{7965:function(t,e,n){},"7dc2":function(t,e,n){},"938f":function(t,e,n){"use strict";var a=n("7dc2"),o=n.n(a);o.a},9406:function(t,e,n){"use strict";n.r(e);var a=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"dashboard-container"},[n("panel-group",{on:{handleSetLineChartData:t.handleSetLineChartData}}),t._v(" "),n("el-row",{attrs:{gutter:8}},[n("el-col",{staticStyle:{"padding-right":"8px","margin-bottom":"30px"},attrs:{xs:{span:24},sm:{span:24},md:{span:24},lg:{span:12},xl:{span:12}}},[n("transaction-table")],1),t._v(" "),n("el-col",{staticStyle:{"padding-right":"8px","margin-bottom":"30px"},attrs:{xs:{span:24},sm:{span:24},md:{span:24},lg:{span:12},xl:{span:12}}},[n("todo-list")],1)],1),t._v(" "),n("el-dialog",{attrs:{title:"提示",visible:t.dialog,width:"30%"}},[n("span",[t._v("是否去完善用户信息？")]),t._v(" "),n("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{on:{click:function(e){t.dialog=!1}}},[t._v("取 消")]),t._v(" "),n("el-button",{attrs:{type:"primary"},on:{click:t.gotoUserInfo}},[t._v("确 定")])],1)])],1)},o=[],i=(n("8e6e"),n("ac6a"),n("456d"),n("bd86")),s=n("2f62"),r=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("el-row",{staticClass:"panel-group",attrs:{gutter:40}},[n("el-col",{staticClass:"card-panel-col",attrs:{xs:12,sm:12,lg:6}},[n("div",{staticClass:"card-panel",on:{click:function(e){return t.handleSetLineChartData("postedTask")}}},[n("div",{staticClass:"card-panel-icon-wrapper icon-posted-task"},[n("svg-icon",{attrs:{"icon-class":"task","class-name":"card-panel-icon"}})],1),t._v(" "),n("div",{staticClass:"card-panel-description"},[n("div",{staticClass:"card-panel-text"},[t._v("\n          已发布任务\n        ")]),t._v(" "),n("count-to",{staticClass:"card-panel-num",attrs:{"start-val":0,"end-val":this.postedNum,duration:2600}})],1)])]),t._v(" "),n("el-col",{staticClass:"card-panel-col",attrs:{xs:12,sm:12,lg:6}},[n("div",{staticClass:"card-panel",on:{click:function(e){return t.handleSetLineChartData("receivedTask")}}},[n("div",{staticClass:"card-panel-icon-wrapper icon-received-task"},[n("svg-icon",{attrs:{"icon-class":"task","class-name":"card-panel-icon"}})],1),t._v(" "),n("div",{staticClass:"card-panel-description"},[n("div",{staticClass:"card-panel-text"},[t._v("\n          已接收任务\n        ")]),t._v(" "),n("count-to",{staticClass:"card-panel-num",attrs:{"start-val":0,"end-val":this.receivedNum,duration:2600}})],1)])]),t._v(" "),n("el-col",{staticClass:"card-panel-col",attrs:{xs:12,sm:12,lg:6}},[n("div",{staticClass:"card-panel",on:{click:function(e){return t.handleSetLineChartData("reputation")}}},[n("div",{staticClass:"card-panel-icon-wrapper icon-reputation"},[n("svg-icon",{attrs:{"icon-class":"rep","class-name":"card-panel-icon"}})],1),t._v(" "),n("div",{staticClass:"card-panel-description"},[n("div",{staticClass:"card-panel-text"},[t._v("\n          荣誉\n        ")]),t._v(" "),n("count-to",{staticClass:"card-panel-num",attrs:{"start-val":0,"end-val":this.reputation,duration:2600}})],1)])]),t._v(" "),n("el-col",{staticClass:"card-panel-col",attrs:{xs:12,sm:12,lg:6}},[n("div",{staticClass:"card-panel",on:{click:function(e){return t.handleSetLineChartData("reward")}}},[n("div",{staticClass:"card-panel-icon-wrapper icon-reward"},[n("svg-icon",{attrs:{"icon-class":"reward","class-name":"card-panel-icon"}})],1),t._v(" "),n("div",{staticClass:"card-panel-description"},[n("div",{staticClass:"card-panel-text"},[t._v("\n          余额\n        ")]),t._v(" "),n("count-to",{staticClass:"card-panel-num",attrs:{"start-val":0,"end-val":this.balance,duration:2600}})],1)])])],1)},c=[],l=n("ec1b"),d=n.n(l),u=n("ad8f"),p=n("c24f"),f={name:"PanelGroup",data:function(){return{postedNum:-1,receivedNum:-1,reputation:70,balance:-1}},components:{CountTo:d.a},created:function(){this.fetchData()},methods:{fetchData:function(){var t=this;Object(u["a"])({type:"post"}).then((function(e){t.postedNum=e.data.length})),Object(u["a"])({type:"received"}).then((function(e){t.receivedNum=e.data.length})),Object(p["d"])().then((function(e){t.reputation=e.data.reputation})),Object(p["c"])().then((function(e){t.balance=e.data}))},handleSetLineChartData:function(t){this.$emit("handleSetLineChartData",t)}}},h=f,v=(n("938f"),n("2877")),g=Object(v["a"])(h,r,c,!1,null,"5ec98d92",null),m=g.exports,b=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{class:t.className,style:{height:t.height,width:t.width}})},C=[],_=n("313e"),y=n.n(_);n("817d");var k={props:{legendName:{type:Array,default:["expected","actual"]},className:{type:String,default:"chart"},width:{type:String,default:"100%"},height:{type:String,default:"350px"},autoResize:{type:Boolean,default:!0},chartData:{type:Object,required:!0}},data:function(){return{chart:null}},watch:{chartData:{deep:!0,handler:function(t){this.setOptions(t)}}},mounted:function(){var t=this;this.$nextTick((function(){t.initChart()}))},beforeDestroy:function(){this.chart&&(this.chart.dispose(),this.chart=null)},methods:{initChart:function(){this.chart=y.a.init(this.$el,"macarons"),this.setOptions(this.chartData)},setOptions:function(){var t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{},e=t.expectedData;this.chart.setOption({xAxis:{data:["Mon","Tue","Wed","Thu","Fri","Sat","Sun"],boundaryGap:!1,axisTick:{show:!1}},grid:{left:10,right:10,bottom:20,top:30,containLabel:!0},tooltip:{trigger:"axis",axisPointer:{type:"cross"},padding:[5,10]},yAxis:{axisTick:{show:!1}},legend:{data:this.legendName},series:[{name:this.legendName[0],itemStyle:{normal:{color:"#FF005A",lineStyle:{color:"#FF005A",width:2}}},smooth:!0,type:"line",data:e,animationDuration:2800,animationEasing:"cubicInOut"}]})}}},w=k,x=Object(v["a"])(w,b,C,!1,null,null,null),T=x.exports,O=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("el-card",[n("div",{attrs:{slot:"header"},slot:"header"},[n("el-tag",[t._v("最新发布")])],1),t._v(" "),n("el-table",{staticStyle:{width:"100%","padding-top":"15px"},attrs:{data:t.list,title:"最新发布"}},[n("el-table-column",{attrs:{label:"序号","min-width":"30",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v("\n        "+t._s(e.row.id)+"\n      ")]}}])}),t._v(" "),n("el-table-column",{attrs:{label:"标题","min-width":"130",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v("\n        "+t._s(e.row.title)+"\n      ")]}}])}),t._v(" "),n("el-table-column",{attrs:{label:"状态","min-width":"100",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[n("el-button",{attrs:{round:"",type:t._f("taskStatusFilter")(e.row.status)}},[t._v("\n          "+t._s(e.row.status)+"\n        ")])]}}])})],1)],1)},D=[];n("6b54"),n("a481"),n("b775");var S={name:"TransactionTable",filters:{taskStatusFilter:function(t){var e={PENDING:"primary",UNACCEPTED:"success",ACCEPTED:"info",EVALUATING:"danger",COMPLETED:"warning"};return e[t]},statusFilter:function(t){var e={finished:"success",accepted:"danger"};return e[t]},orderNoFilter:function(t){return t.substring(0,30)},toThousandFilter:function(t){return(+t||0).toString().replace(/^-?\d+/g,(function(t){return t.replace(/(?=(?!\b)(\d{3})+$)/g,",")}))}},data:function(){return{list:null}},created:function(){this.fetchData()},methods:{fetchData:function(){var t=this;Object(u["a"])({isall:!0}).then((function(e){t.list=e.data.slice(0,4)}))}}},j=S,E=Object(v["a"])(j,O,D,!1,null,"db21dc00",null),L=E.exports,N=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("section",{staticClass:"todoapp"},[n("header",{staticClass:"header"},[n("input",{staticClass:"new-todo",attrs:{autocomplete:"off",placeholder:"Todo List"},on:{keyup:function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.addTodo(e)}}})]),t._v(" "),n("section",{directives:[{name:"show",rawName:"v-show",value:t.todos.length,expression:"todos.length"}],staticClass:"main"},[n("input",{staticClass:"toggle-all",attrs:{id:"toggle-all",type:"checkbox"},domProps:{checked:t.allChecked},on:{change:function(e){return t.toggleAll({done:!t.allChecked})}}}),t._v(" "),n("label",{attrs:{for:"toggle-all"}}),t._v(" "),n("ul",{staticClass:"todo-list"},t._l(t.filteredTodos,(function(e,a){return n("todo",{key:a,attrs:{todo:e},on:{toggleTodo:t.toggleTodo,editTodo:t.editTodo,deleteTodo:t.deleteTodo}})})),1)]),t._v(" "),n("footer",{directives:[{name:"show",rawName:"v-show",value:t.todos.length,expression:"todos.length"}],staticClass:"footer"},[n("span",{staticClass:"todo-count"},[n("strong",[t._v(t._s(t.remaining))]),t._v("\n      "+t._s(t._f("pluralize")(t.remaining,"item"))+" left\n    ")]),t._v(" "),n("ul",{staticClass:"filters"},t._l(t.filters,(function(e,a){return n("li",{key:a},[n("a",{class:{selected:t.visibility===a},on:{click:function(e){e.preventDefault(),t.visibility=a}}},[t._v(t._s(t._f("capitalize")(a)))])])})),0)])])},P=[],$=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("li",{staticClass:"todo",class:{completed:t.todo.done,editing:t.editing}},[n("div",{staticClass:"view"},[n("input",{staticClass:"toggle",attrs:{type:"checkbox"},domProps:{checked:t.todo.id},on:{change:function(e){return t.toggleTodo(t.todo)}}}),t._v(" "),n("label",{domProps:{textContent:t._s(t.todo.title)},on:{dblclick:function(e){t.editing=!0}}}),t._v(" "),n("button",{staticClass:"destroy",on:{click:function(e){return t.deleteTodo(t.todo)}}})]),t._v(" "),n("input",{directives:[{name:"show",rawName:"v-show",value:t.editing,expression:"editing"},{name:"focus",rawName:"v-focus",value:t.editing,expression:"editing"}],staticClass:"edit",domProps:{value:t.todo.title},on:{keyup:[function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.doneEdit(e)},function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"esc",27,e.key,["Esc","Escape"])?null:t.cancelEdit(e)}],blur:t.doneEdit}})])},A=[],F={name:"Todo",directives:{focus:function(t,e,n){var a=e.value,o=n.context;a&&o.$nextTick((function(){t.focus()}))}},props:{todo:{type:Object,default:function(){return{}}}},data:function(){return{editing:!1}},methods:{deleteTodo:function(t){this.$emit("deleteTodo",t)},editTodo:function(t){var e=t.todo,n=t.value;this.$emit("editTodo",{todo:e,value:n})},toggleTodo:function(t){this.$emit("toggleTodo",t)},doneEdit:function(t){var e=t.target.value.trim(),n=this.todo;e?this.editing&&(this.editTodo({todo:n,value:e}),this.editing=!1):this.deleteTodo({todo:n})},cancelEdit:function(t){t.target.value=this.todo.text,this.editing=!1}}},I=F,z=Object(v["a"])(I,$,A,!1,null,null,null),G=z.exports,U="todos",J={all:function(t){return t},active:function(t){return t.filter((function(t){return!t.done}))},completed:function(t){return t.filter((function(t){return t.done}))}},q={components:{Todo:G},filters:{pluralize:function(t,e){return 1===t?e:e+"s"},capitalize:function(t){return t.charAt(0).toUpperCase()+t.slice(1)}},data:function(){return{visibility:"all",filters:J,todos:[]}},computed:{allChecked:function(){return this.todos.every((function(t){return t.done}))},filteredTodos:function(){return J[this.visibility](this.todos)},remaining:function(){return this.todos.filter((function(t){return!t.done})).length}},created:function(){this.fetchData()},methods:{fetchData:function(){var t=this;Object(u["a"])({type:"received"}).then((function(e){t.todos=e.data}))},setLocalStorage:function(){window.localStorage.setItem(U,JSON.stringify(this.todos))},addTodo:function(t){var e=t.target.value;e.trim()&&(this.todos.push({text:e,done:!1}),this.setLocalStorage()),t.target.value=""},toggleTodo:function(t){t.done=!t.done,this.setLocalStorage()},deleteTodo:function(t){this.todos.splice(this.todos.indexOf(t),1),this.setLocalStorage()},editTodo:function(t){var e=t.todo,n=t.value;e.text=n,this.setLocalStorage()},clearCompleted:function(){this.todos=this.todos.filter((function(t){return!t.done})),this.setLocalStorage()},toggleAll:function(t){var e=this,n=t.done;this.todos.forEach((function(t){t.done=n,e.setLocalStorage()}))}}},M=q,B=(n("ef4b"),Object(v["a"])(M,N,P,!1,null,null,null)),R=B.exports;function V(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(t);e&&(a=a.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),n.push.apply(n,a)}return n}function W(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{};e%2?V(Object(n),!0).forEach((function(e){Object(i["a"])(t,e,n[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(n)):V(Object(n)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(n,e))}))}return t}var H={postedTask:{expectedData:[0,0,0,0,0,0,0]},receivedTask:{expectedData:[0,0,0,0,0,0,0]},reputation:{expectedData:[70,70,70,70,70,70,70]},reward:{expectedData:[0,0,0,0,0,0,0]}},K={name:"Dashboard",components:{TransactionTable:L,LineChart:T,PanelGroup:m,TodoList:R},data:function(){return{dialog:!1,legendName:["PostedTask"],lineChartData:H.newTransaction}},computed:W({},Object(s["b"])(["name"])),methods:{handleSetLineChartData:function(t){this.lineChartData=H[t],this.legendName[0]=t},gotoUserInfo:function(){this.dialog=!1,this.$router.push({path:"/setting/setting"})}},created:function(){this.handleSetLineChartData("postedTask")},mounted:function(){"register"===this.$route.query.type&&(this.dialog=!0)}},Q=K,X=(n("f8bf"),Object(v["a"])(Q,a,o,!1,null,"39c9e6e6",null));e["default"]=X.exports},ad8f:function(t,e,n){"use strict";n.d(e,"a",(function(){return o})),n.d(e,"b",(function(){return i}));var a=n("b775");function o(t){return Object(a["a"])({url:"/api/task",method:"get",params:t})}function i(t){return Object(a["a"])({url:"/api/task",method:"post",data:t})}},ef4b:function(t,e,n){"use strict";var a=n("7965"),o=n.n(a);o.a},f3b1:function(t,e,n){},f8bf:function(t,e,n){"use strict";var a=n("f3b1"),o=n.n(a);o.a}}]);