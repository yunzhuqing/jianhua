$(function () {
    getGrid();
    initialPage();
});

function initialPage() {
    $(window).resize(function () {
        TreeGrid.table.resetHeight({height: $(window).height() - 100});
    });
}

function getGrid() {
    var columns = TreeGrid.initColumn();
    var table = new TreeTable(TreeGrid.id, '../scene/list', columns);
    table.setExpandColumn(2);
    table.setIdField("id");
    table.setCodeField("id");
    table.setParentCodeField("parentId");
    table.setExpandAll(false);
    table.setHeight($(window).height() - 100);
    table.init();
    TreeGrid.table = table;
}

var TreeGrid = {
    id: "jqGrid",
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
TreeGrid.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', align: 'center', valign: 'middle', width: '100px'},
        {title: '场景名称', field: 'name', align: 'center', valign: 'middle', width: '100px'},
        {title: '创建人', field: 'userName', align: 'center', valign: 'middle', width: '100px'},
        {title: '创建时间', field: 'ct', align: 'center', valign: 'middle', width: '100px'}];
    return columns;
};

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        scene:{},
        parentScenes:[]
    },
    mounted() {
        this.getParentScenes();
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
            var id = TreeGrid.table.getSelectedRow();
            if (id.length == 0) {
                iview.Message.error("请选择一条记录");
                return;
            }
            vm.showList = false;
            vm.title = "修改";
            vm.getInfo(id[0].id);
        },
        getInfo: function (id) {
            Ajax.request({
                url: "../scene/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.scene = r.scene;
                }
            });
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
            TreeGrid.table.refresh();
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