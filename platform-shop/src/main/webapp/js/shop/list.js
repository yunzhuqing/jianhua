$(function () {
    $("#jqGrid").Grid({
        url: '../shops/list',
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true, hidden: true},
            {label: '店铺名称', name: 'shopName', index: 'shopName', width: 80},
            {label: '店铺地址', name: 'shopAddress', index: 'shopAddress', width: 160},
            {label: '创建人', name: 'userId', index: 'userId', width: 120},
            {label: '店铺描述', name: 'shopDesc', index: 'shopDesc', width: 80},
            {label: '店铺图片', name: 'shopPic', index: 'shopPic', width: 80},
            {label: '店铺状态', name: 'shopState', index: 'shopState', width: 80},
            {label: '店铺创建时间', name: 'createTime', index: 'createTime', width: 80}]
    });
});

var ztree;

var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url: "nourl"
        }
    }
};
var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        uploadList: [],
        imgName: '',
        visible: false,
        shop: {
            id: null,
            shopName: '',
            shopAddress: '',
            userId: '',
            shopDesc: '',
            shopPic: '',
            shopSubPic: '',
            shopState: 0,
            createTime: 0
        },
        ruleValidate: {
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.uploadList = [];
        },
        update: function (event) {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";
            vm.uploadList = [];
            vm.getInfo(id);
        },
        /**
         * 获取品牌
         */
        getBrands: function () {
            Ajax.request({
                url: "../brand/queryAll",
                async: true,
                successCallback: function (r) {
                    vm.brands = r.list;
                }
            });
        },
        /**
         * 获取单位
         */
        getMacro: function () {
            Ajax.request({
                url: "../sys/macro/queryMacrosByValue?value=goodsUnit",
                async: true,
                successCallback: function (r) {
                    vm.macros = r.list;
                }
            });
        },
        getGoodsGallery: function (id) {//获取商品顶部轮播图
            Ajax.request({
                url: "../goodsgallery/queryAll?goodsId=" + id,
                async: true,
                successCallback: function (r) {
                    vm.uploadList = r.list;
                }
            });
        },
        getAttributeCategories: function () {
            Ajax.request({
                url: "../attributecategory/queryAll",
                async: true,
                successCallback: function (r) {
                    vm.attributeCategories = r.list;
                }
            });
        },
        saveOrUpdate: function (event) {
            var url = vm.shop.id == null ? "../shops/save" : "../shops/update";
            Ajax.request({
                type: "POST",
                url: url,
                contentType: "application/json",
                params: JSON.stringify(vm.shop),
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
            });
        },
        enSale: function () {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            confirm('确定要上架选中的商品？', function () {
                Ajax.request({
                    type: "POST",
                    url: "../goods/enSale",
                    params: JSON.stringify(id),
                    contentType: "application/json",
                    type: 'POST',
                    successCallback: function () {
                        alert('提交成功', function (index) {
                            vm.reload();
                        });
                    }
                });
            });
        },
        openSpe: function () {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            openWindow({
                type: 2,
                title: '商品规格',
                content: '../shop/goodsspecification.html?goodsId=' + id
            })
        },
        openPro: function () {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            openWindow({
                type: 2,
                title: '产品设置',
                content: '../shop/product.html?goodsId=' + id
            });
        },
        unSale: function () {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            confirm('确定要下架选中的商品？', function () {

                Ajax.request({
                    type: "POST",
                    url: "../goods/unSale",
                    contentType: "application/json",
                    params: JSON.stringify(id),
                    successCallback: function (r) {
                        alert('操作成功', function (index) {
                            vm.reload();;
                        });
                    }
                });

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
                    url: "../goods/delete",
                    contentType: "application/json",
                    params: JSON.stringify(ids),
                    successCallback: function (r) {
                        alert('操作成功', function (index) {
                            vm.reload();;
                        });
                    }
                });

            });
        },
        getInfo: function (id) {
            Ajax.request({
                url: "../shops/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.shop = r.shop;
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
        },
        getCategory: function () {
            //加载分类树
            Ajax.request({
                url: "../category/queryAll",
                async: true,
                successCallback: function (r) {
                    ztree = $.fn.zTree.init($("#categoryTree"), setting, r.list);
                    var node = ztree.getNodeByParam("id", vm.goods.categoryId);
                    if (node) {
                        ztree.selectNode(node);
                        vm.goods.categoryName = node.name;
                    } else {
                        node = ztree.getNodeByParam("id", 0);
                        ztree.selectNode(node);
                        vm.goods.categoryName = node.name;
                    }
                }
            });
        },
        categoryTree: function () {
            openWindow({
                title: "选择类型",
                area: ['300px', '450px'],
                content: jQuery("#categoryLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级菜单
                    vm.goods.categoryId = node[0].id;
                    vm.goods.categoryName = node[0].name;

                    layer.close(index);
                }
            });
        },
        handleView(name) {
            this.imgName = name;
            this.visible = true;
        },
        handleRemove(file) {
            // 从 upload 实例删除数据
            const fileList = this.uploadList;
            this.uploadList.splice(fileList.indexOf(file), 1);
        },
        handleSuccess(res, file) {
            // 因为上传过程为实例，这里模拟添加 url
            file.imgUrl = res.url;
            file.name = res.url;
            vm.uploadList.add(file);
        },
        handleBeforeUpload() {
            const check = this.uploadList.length < 5;
            if (!check) {
                this.$Notice.warning({
                    title: '最多只能上传 5 张图片。'
                });
            }
            return check;
        },
        handleSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.saveOrUpdate()
            });
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
        handleReset: function (name) {
            handleResetForm(this, name);
        },
        handleSuccessPicUrl: function (res, file) {
            vm.shop.shopPic = file.response.url;
        },
        handleSuccessListPicUrl: function (res, file) {
            vm.shop.shopSubPic = file.response.url;
        },
        eyeImagePicUrl: function () {
            var url = vm.shop.shopPic;
            eyeImage(url);
        },
        eyeImageListPicUrl: function () {
            var url = vm.shop.shopSubPic;
            eyeImage(url);
        },
        eyeImage: function (e) {
            eyeImage($(e.target).attr('src'));
        }
    }
});