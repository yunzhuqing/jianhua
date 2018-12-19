$(function () {
    var sceneId=getQueryString('sceneId');
    $("#jqGrid").Grid({
        url: '../scenegroup/list?sceneId=' + sceneId,
        colModel: [{
            label: 'id', name: 'id', index: 'id', key: true, hidden: true
        }, {
            label: '商品名称', name: 'name', index: 'name', width: 80
        }, {
            label: '商品类型', name: 'type', index: 'type', width: 80
        }, {
            label: '商品图片', name: 'pictureUrl', index: 'pictureUrl', width: 80, formatter: function (value) {
                return transImg(value);
            }
        }, {
            label: '商品顺序', name: 'location', index: 'location', width: 80
        }, {
            label: '创建时间', name: 'createTime', index: 'createTime', width: 80, formatter: function (value) {
                return transDate(value);
            }
        }, {
            label: '更新时间', name: 'updateTime', index: 'updateTime', width: 80, formatter: function (value) {
                return transDate(value);
            }
        }, {
            label: '创建人', name: 'userName', index: 'userName', width: 80
        }]
    });
});


var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        sceneGroup:{},
        parentScenes:[]
    },
    mounted() {
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
                return false;
            }
            vm.showList = false;
            vm.title = "修改";
            vm.getInfo(id);
        },
        getInfo: function (id) {
            Ajax.request({
                url: "../scenegroup/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.sceneGroup = r.sceneGroup;
                }
            });
        },
        saveOrUpdate: function (event) {
            vm.sceneGroup.sid = getQueryString('sceneId');
            var url = vm.sceneGroup.id == null ? "../scenegroup/save" : "../scenegroup/update";

            Ajax.request({
                type: "POST",
                url: url,
                contentType: "application/json",
                params: JSON.stringify(vm.sceneGroup),
                successCallback: function () {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
            });
        },
        addGoods: function() {
            console.log('add goods');
        },
        del: function (event) {
            var ids = getSelectedRows("#jqGrid");
            if (ids == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {

                Ajax.request({
                    type: "POST",
                    url: "../sceneGroup/delete",
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
        getParentScenes: function () {
            Ajax.request({
                url: "../scene/getParent" ,
                async: true,
                successCallback: function (r) {
                    vm.parentScenes = r.scenes;
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: null,
                page: page
            }).trigger("reloadGrid");
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
