$(function () {
    let goodsId = getQueryString("goodsId");
    let url = '../product/list';
    if (goodsId) {
        url += '?goodsId=' + goodsId;
    }
    $("#jqGrid").Grid({
        url: url,
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true, hidden: true},
            {label: '商品', name: 'goodsName', index: 'goods_id', width: 120},
            {
                label: '商品规格',
                name: 'specificationValue',
                index: 'goods_specification_ids',
                width: 100,
                formatter: function (value, options, row) {
                    return value.replace(row.goodsName + " ", '');
                }
            },
            {label: '商品序列号', name: 'goodsSn', index: 'goods_sn', width: 80},
            {label: '商品库存', name: 'goodsNumber', index: 'goods_number', width: 80},
            {label: '零售价格(元)', name: 'retailPrice', index: 'retail_price', width: 80},
            {label: '市场价格(元)', name: 'marketPrice', index: 'market_price', width: 80}]
    });
});

let vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        product: {
            items:[
                {
                    attrId:0,
                    specId:0
                }
            ]
        },
        ruleValidate: {
            name: [
                {required: true, message: '名称不能为空', trigger: 'blur'}
            ]
        },
        q: {
            goodsName: ''
        },
        goodss: [],
        attribute: {
            id:[]
        },
        attributes:{},
        productSpecs:{},
        type: '',
        spec:{},
        specs:[],
        varList:{}
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.product = {};
            vm.getGoodss();
            vm.type = 'add';
        },
        update: function (event) {
            let id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";
            vm.type = 'update';
            vm.getInfo(id);
        },
        changeGoods: function (opt) {
            let goodsId = opt.value;
            if(!goodsId)return;

            Ajax.request({
                url: "../goods/info/" + goodsId,
                async: true,
                successCallback: function (r) {
                    if (vm.type == 'add') {
                        vm.product.goodsSn = r.goods.goodsSn;
                        vm.product.goodsNumber = r.goods.goodsNumber;
                        vm.product.retailPrice = r.goods.retailPrice;
                        vm.product.marketPrice = r.goods.marketPrice;
                    }
                    Ajax.request({
                        url: "../specification/queryAll",
                        async: false,
                        successCallback: function (r) {
                            vm.specs = r.list;
                            vm.specs.map(function (n) {
                                vm.attributes[n.id]=[];
                            });
                        }
                    });
                }
            });
        },
        changeSpec:function(opt){
            let goodsId=vm.product.goodsId;
            if(null == goodsId) {
                alert('请选择商品');
                return;
            }
            let specId=opt
            if(null == specId) {
                alert("请指定规格");
                return;
            }

            Ajax.request({
                url: "../goodsspecification/queryAll?goodsId=" + goodsId + "&specificationId=" + specId,
                async: false,
                successCallback: function (r) {
                    vm.varList[specId] = r.list;
                }
            });
        },
        changeAttribute: function(opt) {
            let specId=vm.spec.id
            if(null==specId) {
                alert('特征为空');
                return;
            }

            if(null==vm.productSpecs[specId]) {
                vm.productSpecs[specId]=[opt];
            } else {
                var arr =[];
                vm.productSpecs[specId].map(function (n) {
                    if(n!=opt){
                        arr.push(n);
                    }
                });
                vm.productSpecs[specId]=arr;
            }
        },
        saveOrUpdate: function (event) {
            let url = vm.product.id == null ? "../product/save" : "../product/update";

            let cnt="";
            for(let key in vm.attributes) {
                let arr=vm.attributes[key];
                cnt += key;
                cnt += "-";
                ids = "";
                arr.map(function (n) {
                    ids = n + "_" + ids;
                });
                cnt = cnt + ids + ",";
            }

            vm.product.goodsSpecificationIds = cnt;

            Ajax.request({
                type: "POST",
                url: url,
                contentType: "application/json",
                params: JSON.stringify(vm.product),
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
            });
        },
        del: function (event) {
            let ids = getSelectedRows("#jqGrid");
            if (ids == null) {
                return;
            }
            confirm('确定要删除选中的记录？', function () {
                Ajax.request({
                    type: "POST",
                    url: "../product/delete",
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
            vm.attribute = [];
            Ajax.request({
                url: "../product/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.product = r.product;
                    let goodsSpecificationIds = vm.product.goodsSpecificationIds.split("_");

                    let varList = {}, attrArr=[];
                    goodsSpecificationIds.forEach((goodsSpecification, index) => {
                        let slices = goodsSpecification.split("-");
                        let specId = slices[0];
                        let attrs = slices[1];
                        if(attrs != null && attrs != "") {
                            attrArr = attrs.split("_");
                        }
                        varList[specId]=attrArr;
                    });
                    vm.varList=varList;

                    vm.getGoodss();
                }
            });
            vm.spec.id=0;
        },
        reload: function (event) {
            vm.showList = true;
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'goodsName': vm.q.goodsName},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
        },
        handleSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.saveOrUpdate()
            });
        },
        handleReset: function (name) {
            handleResetForm(this, name);
        },
        getGoodss: function () {
            Ajax.request({
                url: "../goods/queryAll/",
                async: true,
                successCallback: function (r) {
                    vm.goodss = r.list;
                }
            });
        }
    }
});