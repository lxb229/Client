/**
 * Created by Administrator on 2017/8/7.
 */

require(['vue','layui'], function (Vue) {
    var dept = new Vue({
        el: "#addstudent",
        data:{

        },
        methods:{
//���Ȩ��
            addStu:function(){
                gOpenModalWindow("�½��˺�", "system/userForm.html", 450, 250);

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
                    //���õ�ǰ�����е�Id
                    this.rowtemplate.Id = this.rows.length + 1;
                    this.rows.push(this.rowtemplate);
                }

                //��ԭģ��
                this.rowtemplate = { Id: 0, Name: '', Age: '', School: '', Remark: '' }
            },
            Delete: function (id) {
                //ʵ����Ŀ�в��������϶����漰��idȥ��̨ɾ��������ֻ��չʾ������ô����
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