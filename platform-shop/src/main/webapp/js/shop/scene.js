$(function () {
    $("#jqGrid").Grid({
        url: '../scene/list',
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true, hidden: true},
            {label: '场景名称', name: 'name', index: 'name', width: 80},
            {label: '创建人', name: 'userName', index: 'userName', width: 80},
            {label: '创建时间', name: 'ct', index: 'ct', width: 80}
        ]
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        scene:{}
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
        },
        update: function (event) {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id);
            this.getAdPositions();
        },
        saveOrUpdate: function (event) {
            var url = vm.scene.id == null ? "../scene/save" : "../scene/update";

            Ajax.request({
                type: "POST",
                url: url,
                contentType: "application/json",
                params: JSON.stringify(vm.scene),
                successCallback: function () {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
            });
        },
        del: function (event) {
            var ids = getSelectedRows("#jqGrid");
            if (ids == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {

                Ajax.request({
                    type: "POST",
                    url: "../scene/delete",
                    contentType: "application/json",
                    params: JSON.stringify(ids),
                    successCallback: function () {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    }
                });
            });
        },
        getInfo: function (id) {
            Ajax.request({
                url: "../ad/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.ad = r.ad;
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'name': vm.q.name},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
        },
        handleSuccess: function (res, file) {
            vm.ad.imageUrl = file.response.url;
        },
        handleFormatError: function (file) {
            this.$Notice.warning({
                title: '文件格式不正确',
                desc: '文件 ' + file.name + ' 格式不正确，请上传 jpg 或 png 格式的图片。'
            });
        },
        handleMaxSize: function (file) {
            this.$Notice.warning({
                title: '超出文件大小限制',
                desc: '文件 ' + file.name + ' 太大，不能超过 2M。'
            });
        },
        handleSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.saveOrUpdate()
            });
        },
        handleReset: function (name) {
            handleResetForm(this, name);
        },
        eyeImage: function () {
            var url = vm.ad.imageUrl;
            eyeImage(url);
        },
        /**
         * 获取会员级别
         */
        getAdPositions: function () {
            Ajax.request({
                url: "../adposition/queryAll",
                async: true,
                successCallback: function (r) {
                    vm.adPositions = r.list;
                }
            });
        }
    }
});