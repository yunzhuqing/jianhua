$(function () {
    let goodsId = getQueryString("goodsId");
    let url = '../';
    if (goodsId) {
        url += '?goodsId=' + goodsId;
    }
    $("#jqGrid").Grid({
        url: url,
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true, hidden: true},
            {label: '商品', name: 'goodsName', index: 'goods_id', width: 80},
            {label: '规格', name: 'specificationName', index: 'specification_id', width: 80},
            {label: '规格说明', name: 'value', index: 'value', width: 80},
            {label: '剩余库存', name: 'leftInventory', index: 'leftInventory', width: 80},
            {
                label: '规格图片', name: 'picUrl', index: 'pic_url', width: 80, formatter: function (value) {
                    return transImg(value);
                }
            }]
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        goodsSpecification: {attributeIds:{}},
        ruleValidate: {
            name: [
                {required: true, message: '名称不能为空', trigger: 'blur'}
            ]
        },
        q: {
            name: ''
        },
        goodss: [],
        specifications: [],
        attributes: [],
        attrVals: {}
    },
    methods: {
        getSpecification: function () {
        },
        getAttribute: function(id) {
            var attribute = null;
            //获取选中商品对应的类目
            Ajax.request({
                url: "../attribute/info/" + id,
                async: false,
                successCallback: function (r) {
                    attribute = r.attribute;
                }
            });
            return attribute;
        },
        getAttrVal: function(id) {
            var attrVal = null;
            //获取选中商品对应的类目
            Ajax.request({
                url: "../attribute/val/info/" + id,
                async: false,
                successCallback: function (r) {
                    attrVal = r.data;
                }
            });
            return attrVal;
        },
        selectGoods: function(e) {
            vm.attributes=[];
            vm.attrVals={};
            vm.goodsSpecification.attributeIds = {};
            Ajax.request({
                url: "../attribute/val/listAttrs?productId=" + e,
                async: true,
                successCallback: function (r) {
                    for(var key in r.data) {
                        vm.goodsSpecification.attributeIds[key]="";
                        var attribute = vm.getAttribute(key);
                        vm.attributes.push(attribute);
                        var list = r.data[key];
                        if(null != list) {
                            vm.attrVals[key] = [];
                            for(var i=0; i<list.length; i++) {
                                vm.attrVals[key].push(vm.getAttrVal(list[i]));
                            }
                        }
                    }
                }
            });
        },
        getGoodss: function () {
            Ajax.request({
                url: "../goods/queryAll/",
                async: true,
                successCallback: function (r) {
                    vm.goodss = r.list;
                }
            });
        },
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.goodsSpecification = {};
            vm.getSpecification();
            vm.getGoodss();
        },
        update: function (event) {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
        },
        saveOrUpdate: function (event) {
            var url = "../sku/upsert";

            Ajax.request({
                type: "POST",
                url: url,
                contentType: "application/json",
                params: JSON.stringify(vm.goodsSpecification),
                successCallback: function (r) {
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
                    url: "../goodsspecification/delete",
                    contentType: "application/json",
                    params: JSON.stringify(ids),
                    successCallback: function (r) {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    }
                });

            });
        },
        getInfo: function (id) {
            Ajax.request({
                url: "../goodsspecification/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.goodsSpecification = r.goodsSpecification;
                    vm.getSpecification();
                    vm.getGoodss();
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
        handleSuccess: function (res, file) {
            vm.goodsSpecification.picUrl = file.response.url;
        },
        eyeImage: function () {
            var url = vm.goodsSpecification.picUrl;
            eyeImage(url);
        },
        handleSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.saveOrUpdate()
            });
        },
        handleReset: function (name) {
            handleResetForm(this, name);
        }
    }
});