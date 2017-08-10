var table;
$(document).ready(function() {
	table = $('#tenant_arount_show_table').dataTable({
		fixedHeader: {
	        header: true
	    },
	    "dom": '<lf<t>ip>',
 	    "pagingType": "full_numbers",		// 设置分页控件的模式
 	    "processing": false, 				// 打开数据加载时的等待效果
        "bPaginate": true,                  // 分页设置
        "bLengthChange": true,              //
        "bSort": false,
        "bInfo": true,
        "bAutoWidth": true,
        "ajax":{
     		"url":ctx+"manage/findAllTenantAroundMgr",
        	"data":function(d){
        		d.tenantName = $("#tenantLeaveName").val();
        		d.tenantInterface= $("#tenantInterface").val();
             }
        },
        "columns":[{data:null,"targets":0,},             
                   {data:"tenantId",},  
                   {data:"tenantName",},
                   {data:"tenantLevel",
        				render:function(data, type, row) {
        						 if(data==2){
        							return "大";
        						}else if(data==1){
        							return "中";
        						}else if(data==0){
        							return "小";
        						}
        						return data;
        				}
                   },         
                   {data:"tenantBoss",},
                   {data:"tenantTel", },
                   {data:"numOfUnifiedPlatform",},
                   {data:"numOf4a",},   
                   {data:"tenantReqirement",},
                   {data:"tenantInterface",}, 
                   {
                  	 data:"ttaId",
  	            	 render:function(data, type, row) {
  	            		 return '<td><button class="btn btn-xs btn-danger" value="'+data+'">编辑</button>&nbsp;&nbsp;&nbsp;&nbsp;<button class="btn btn-xs btn-danger" value="'+data+'">删除</button></td>';
  	            	 }}
                  ],
             
        "language": {
	        "paginate": {
	        	"sFirst": "首页",
                "sPrevious": "前一页",
                "sNext": "后一页",
                "sLast": "尾页"
	        },
            "info": "显示_START_到_END_, 共计_TOTAL_条数据",
            "zeroRecords": "无记录",
            "infoEmpty": "共计0",
            "lengthMenu": "每页显示 _MENU_ 记录",
            "infoFiltered": "",
            "processing": "加载中......",
            "sSearch": "查询:  "
        },
        
        //设置序号列
       "fnDrawCallback": function(){
        	var api = this.api();
        	//var startIndex= api.context[0]._iDisplayStart;//获取到本页开始的条数
        	api.column(0).nodes().each(function(cell, i) {
        		cell.innerHTML = i + 1;
        	});
        }
    });
	
	
  //新增表单提交
    $('#addTeantAroundMgrBtn').click(function() {
        var bootstrapValidator = $('#addUserForm').data('bootstrapValidator');
        bootstrapValidator.validate();
        if (bootstrapValidator.isValid()) {
    	 var tenantId= $("#addaround-tenantId").val();
    	 var tenantName=$("#addaround-tenantName").val();
    	 var tenantLevel=$("#addaround-tenantLevel").val();
    	 var tenantBoss=$("#addaround-tenantBoss").val();
    	 var tenantTel=$("#addaround-tenantTel").val();
    	 var numOfUnifiedPlatform=$("#addaround-numOfUnifiedPlatform").val();
    	 var numOf4a=$("#addaround-numOf4a").val();
    	 var tenantReqirement=$("#addaround-tenantReqirement").val();
    	 var tenantInterface=$("#addaround-tenantInterface").val();
    	$.ajax({
      		url :ctx + "manage/saveTenantAroundMgr",
      		type : "post",
      		data: {tenantId:tenantId,tenantName:tenantName,tenantLevel:tenantLevel,
      			tenantBoss:tenantBoss,tenantTel:tenantTel,numOfUnifiedPlatform:numOfUnifiedPlatform,
      			numOf4a:numOf4a,tenantReqirement:tenantReqirement,tenantInterface:tenantInterface,},
      		success : function(data) {
      			//清空表单信息
      			addClean();
      			data = eval("(" + data + ")");
	      			if (data.message == "200") {
	      				clickTable();
	      				alert("添加成功");
	      			} else {
	      				alert("添加失败");
	      			}
      			}
      		});
        }
    });
    
	//点击关闭按钮重新加载表格
    $('#addReloadHTML').click(function(){
    	window.location.reload();
    });
     

        // 编辑表单验证
        $('#updateTenForm').bootstrapValidator({
            message: 'This value is not valid',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
            	tenantTel: {
                    validators: {
                        notEmpty: {
                            message: '电话号码不能为空'
                        },
//                        stringLength: {
//                            min: 11,
//                            max: 11,
//                            message: '请输入11位电话号码'
//                        },
                        regexp: {
                            regexp: /^1[3|5|8]{1}[0-9]{9}$/,
                            message: '请输入正确的电话号码'
                        }
                    }
                },
                numOfUnifiedPlatform:{
                	validators:{
                		notEmpty:{
                			 message:'统一平台数量不能为空'
                		},
                		 numeric: {
                			 message: '统一平台数量只能输入数字'
                		}  
                	}
                },
                numOf4a:{
                	validators:{
                		notEmpty:{
                			message:'4A数量不能为空'
                		},
                		numeric:{
                			message:'4A数量只能输入数字'
                		}
                	}
                }
                
                
            }
        });
    
    //编辑表单提交
    $('#updateTenArrBtn').click(function() {
    	 var bootstrapValidator = $('#updateTenForm').data('bootstrapValidator');
         bootstrapValidator.validate();
         if (bootstrapValidator.isValid()) {
	    	 var ttaId=$("#updatearound-ttaId").val();
	    	 var tenantId= $("#updatearound-tenantId").val();
	    	 var tenantName=$("#updatearound-tenantName").val();
	    	 var tenantLevel=$("#updatearound-tenantLevel").val();
	    	 var tenantBoss=$("#updatearound-tenantBoss").val();
	    	 var tenantTel=$("#updatearound-tenantTel").val();
	    	 var numOfUnifiedPlatform=$("#updatearound-numOfUnifiedPlatform").val();
	    	 var numOf4a=$("#updatearound-numOf4a").val();
	    	 var tenantReqirement=$("#updatearound-tenantReqirement").val();
	    	 var tenantInterface=$("#updatearound-tenantInterface").val();
	        	 $.ajax({
	           		url :ctx+"manage/saveTenantAroundMgr",
	           		type : "post",
	           		data: {ttaId:ttaId,tenantId:tenantId,tenantName:tenantName,tenantName:tenantName,
	           			tenantLevel:tenantLevel,tenantBoss:tenantBoss,tenantTel:tenantTel,numOfUnifiedPlatform:numOfUnifiedPlatform,
	           			numOf4a:numOf4a,tenantReqirement:tenantReqirement,tenantInterface:tenantInterface},
	           		success : function(data) {
	           			updateTenArrClean();
	           			data = eval("(" + data + ")");
	           			}
	           		});
	         }
    });
    
    //编辑点击提交后，重置验证
    function updateTenArrClean(){
    	$('#uploadTenArrModal').modal('hide');
//    	$('#updateTenForm').bootstrapValidator('resetForm', true);
    }
    
    
    //编辑表单关闭
    $('#updateTenArrHTML').click(function(){
    	window.location.reload();
    });
    
    //编辑&删除
    function TenretiredOperate(e) {
       e = e || window.event;
       var tar = e.target || e.srcElement;
       var tarName = tar.tagName;
       var tarHTML = tar.innerHTML;
       var tarval = tar.value;
       console.log(tar);
       console.log(tarName);
       console.log(tarHTML);
       console.log(tarval);
       if (tarName == 'BUTTON') {
           if (tarHTML == '编辑') {
           	$.ajax({
           		 type:"get",
                    url:ctx+"manage/update",
                    data:{"ttaId":tarval},
                    success:function(data){
                     $("#updatearound-ttaId").val(data.ttaId);
                   	 $("#updatearound-tenantId").val(data.tenantId);
                   	 $("#updatearound-tenantName").val(data.tenantName);
                   	 $("#updatearound-tenantLevel").val(data.tenantLevel);
                   	 $("#updatearound-tenantBoss").val(data.tenantBoss);
                   	 $("#updatearound-tenantTel").val(data.tenantTel);
                   	 $("#updatearound-numOfUnifiedPlatform").val(data.numOfUnifiedPlatform);
                   	 $("#updatearound-numOf4a").val(data.numOf4a);
                   	 $("#updatearound-tenantReqirement").val(data.tenantReqirement);
                   	 $("#updatearound-tenantInterface").val(data.tenantInterface);
                    }
           	});
              $('#uploadTenArrModal').modal('show');
          }else if (tarHTML == '删除') {
              $('#alertTenArrModal .modal-body').html('确定要删除？');
              $('#alertTenArrModal').modal('show');
              $('#removeTenArrTool').unbind('click');
              $('#removeTenArrTool').click(function() {
           	$.ajax({
          		    type:"get",
	          		url:ctx + "manage/validateById",
	                data:{"ttaId":tarval},
                   success:function(data){
                   	if(data){
                   		$("#delete-ttaId").val(tarval);
                    	$("#deleteTenArrForm").submit();
                   	}else{ 
                   		layui.use('layer', function(){
                  			var layer = layui.layer;
                  			layer.alert('此类型有工具引用无法删除', {icon: 5});
                  		}); 
                   	}
                   }
           	});
           	$('#alertTenArrModal').modal('hide');
           })
       }
     }
   }
    
    $('#tenant_arount_show_table tbody').unbind('click',TenretiredOperate);
    $('#tenant_arount_show_table tbody').bind('click',TenretiredOperate);
   
   
});

function addClean(){
	$('#addAroundTenantModal').modal('hide');
	$("#addaround-tenantId").val("");
	$("#addaround-tenantName").val("");
	$("#addaround-tenantLevel").val("");
	$("#addaround-tenantBoss").val("");
	$("#addaround-tenantTel").val("");
	$("#addaround-numOfUnifiedPlatform").val("");
	$("#addaround-numOf4a").val("");
	$("#addaround-tenantReqirement").val("");
	$("#addaround-tenantInterface").val("");
}

//新增按钮
function addAroundTenantModal(){
	
	$('#addAroundTenantModal').modal('show');
}

//查询操作按钮
function clickLeaveTable(){
	table.api().ajax.reload();
}


function clickTable(){
	table.api().ajax.reload();
}
//清除查询数据
function cleanLeaveSearch() {
	$("#tenantLeaveName").val("");
	$("#tenantInterface").val("");
	clickLeaveTable();
}

//文件导出
function exportFile() {
	var length = table.api().data().length;
	alert(length);
	if (length < 1) {
		layui.use('layer', function(){
			  var layer = layui.layer;
			  layer.alert('导出数据为空，请您重新选择导出数据', {icon: 5});
		});   
		return;
	}
	location.href=ctx+'manange/exportTenantAroundMgr';
}

function uploadFile(){
 	$('#uploadFileModal').modal('show');
}

function uploadFileSubmit(){
	$('#uploadFileForm').submit();
	$('#uploadFileModal').modal('hide');
	
}
