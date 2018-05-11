/**
 * Created by Administrator on 2017/8/7.
 */

require(['vue','layui'], function (Vue) {
    var dept = new Vue({
        el: "#addstudent",
        data:{

        },
        methods:{
//添加权限
            addStu:function(){
                gOpenModalWindow("新建账号", "system/userForm.html", 450, 250);

            },
            fetchData:function(){
                var options = this.filter;
                alert(options);
                options.pageNo = this.grid.curr_page;
                options.pageSize = this.grid.page_size;
                $.post("/system/roles/list", options, function (rs) {
                    console.log(rs);
                    cb(rs);
                }, "json");
            },
            Save: function (event) {
                if (this.rowtemplate.Id == 0) {
                    //设置当前新增行的Id
                    this.rowtemplate.Id = this.rows.length + 1;
                    this.rows.push(this.rowtemplate);
                }

                //还原模板
                this.rowtemplate = { Id: 0, Name: '', Age: '', School: '', Remark: '' }
            },
            Delete: function (id) {
                //实际项目中参数操作肯定会涉及到id去后台删除，这里只是展示，先这么处理。
                for (var i=0;i<this.rows.length;i++){
                    if (this.rows[i].Id == id) {
                        this.rows.splice(i, 1);
                        break;
                    }
                }
            },
            Edit: function (row) {
                this.rowtemplate = row;
            }

        },
        watch:{

        }

    });

    var layuiForm;
    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form', 'laydate'], function () {
        layuiForm = layui.form();
        layuiForm.render('select');
        layuiForm.render('radio');

    });

});