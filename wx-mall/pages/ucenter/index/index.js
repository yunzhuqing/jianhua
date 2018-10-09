var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var user = require('../../../services/user.js');
var app = getApp();

var tabs = [{
    name: "我的订单"
  },
  {
    name: "我的购物车"
  },
  {
    name: "我的收藏"
  },
  {
    name: "我的地址"
  },
  {
    name: "我的信息"
  },
  {
    name: "意见反馈"
  },
  {
    name: "我的社区"
  }
];

Page({
  data: {
    userInfo: {},
    hasMobile: '',
    tabs: tabs, //展示的数据
    activeIndex: 0, //当前展示的Tab项索引
    currentTab: 0,
    navScrollLeft: 0,
    orderList: [],
    page: 1,
    size: 10,
    loadmoreText: '正在加载更多数据',
    nomoreText: '全部加载完成',
    nomore: false,
    cartGoods: [],
    cartTotal: {
      "goodsCount": 0,
      "goodsAmount": 0.00,
      "checkedGoodsCount": 0,
      "checkedGoodsAmount": 0.00
    },
    isEditCart: false,
    checkedAllStatus: true,
    editCartList: [],
    typeId: 0,
    collectList: [],
    addressList: [],
    fbarray: ['请选择反馈类型', '商品相关', '物流状况', '客户服务', '优惠活动', '功能异常', '产品建议', '其他'],
    fbindex: 0,
    fbcontent: '',
    fbcontentLength: 0,
    fbmobile: ''
  },
  onLoad: function(options) {
    let that = this;
    wx.getSystemInfo({
      success: (res) => {
        this.setData({
          pixelRatio: res.pixelRatio,
          windowHeight: res.windowHeight,
          windowWidth: res.windowWidth
        })
      },
    });

    if (that.data.currentTab == 0) {
      that.getOrderList();
    }
  },
  onReady: function() {

  },
  onShow: function() {

    let userInfo = wx.getStorageSync('userInfo');
    let token = wx.getStorageSync('token');

    // 页面显示
    if (userInfo && token) {
      app.globalData.userInfo = userInfo;
      app.globalData.token = token;
    }

    this.setData({
      userInfo: app.globalData.userInfo,
    });

  },
  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭
  },
  bindGetUserInfo(e) {
    let userInfo = wx.getStorageSync('userInfo');
    let token = wx.getStorageSync('token');
    if (userInfo && token) {
      return;
    }
    if (e.detail.userInfo) {
      //用户按了允许授权按钮
      user.loginByWeixin(e.detail).then(res => {
        this.setData({
          userInfo: res.data.userInfo
        });
        app.globalData.userInfo = res.data.userInfo;
        app.globalData.token = res.data.token;
      }).catch((err) => {
        console.log(err)
      });
    } else {
      //用户按了拒绝按钮
      wx.showModal({
        title: '警告通知',
        content: '您点击了拒绝授权,将无法正常显示个人信息,点击确定重新获取授权。',
        success: function(res) {
          if (res.confirm) {
            wx.openSetting({
              success: (res) => {
                if (res.authSetting["scope.userInfo"]) { ////如果用户重新同意了授权登录
                  user.loginByWeixin(e.detail).then(res => {
                    this.setData({
                      userInfo: res.data.userInfo
                    });
                    app.globalData.userInfo = res.data.userInfo;
                    app.globalData.token = res.data.token;
                  }).catch((err) => {
                    console.log(err)
                  });
                }
              }
            })
          }
        }
      });
    }
  },
  exitLogin: function() {
    wx.showModal({
      title: '',
      confirmColor: '#b4282d',
      content: '退出登录？',
      success: function(res) {
        if (res.confirm) {
          wx.removeStorageSync('token');
          wx.removeStorageSync('userInfo');
          wx.switchTab({
            url: '/pages/index/index'
          });
        }
      }
    })

  },
  switchNav(event) {
    var cur = event.currentTarget.dataset.current;
    //每个tab选项宽度占1/5
    var singleNavWidth = this.data.windowWidth / 5;
    //tab选项居中                            
    this.setData({
      navScrollLeft: (cur - 2) * singleNavWidth
    })
    if (this.data.currentTab == cur) {
      return false;
    } else {
      this.setData({
        currentTab: cur
      })
    }
  },
  switchTab(event) {
    var cur = event.detail.current;
    var singleNavWidth = this.data.windowWidth / 5;
    this.setData({
      currentTab: cur,
      navScrollLeft: (cur - 2) * singleNavWidth
    });

    if (cur == 0) {
      this.getOrderList();
    } else if(cur == 1) {
      this.getCartList();
    } else if (cur == 2) {
      this.getCollectList();
    } else if (cur == 3) {
      this.getAddressList();
    }
  },

  getOrderList() {
    let that = this;

    if (that.data.totalPages <= that.data.page - 1) {
      that.setData({
        nomore: true
      })
      return;
    }

    util.request(api.OrderList, { userId: that.data.userId, page: that.data.page, size: that.data.size }).then(function (res) {
      if (res.errno === 0) {
        that.setData({
          orderList: that.data.orderList.concat(res.data.data),
          page: res.data.currentPage + 1,
          totalPages: res.data.totalPages
        });
        wx.hideLoading();
      }
    });
  },
  payOrder(event) {
    let that = this;
    let orderIndex = event.currentTarget.dataset.orderIndex;
    let order = that.data.orderList[orderIndex];
    wx.redirectTo({
      url: '/pages/pay/pay?orderId=' + order.id + '&actualPrice=' + order.actual_price,
    })
  },

  // 购物车开始
  getCartList: function () {
    let that = this;
    util.request(api.CartList).then(function (res) {
      if (res.errno === 0) {
        console.log(res.data);
        that.setData({
          cartGoods: res.data.cartList,
          cartTotal: res.data.cartTotal
        });
      }

      that.setData({
        checkedAllStatus: that.isCheckedAll()
      });
    });
  },
  isCheckedAll: function () {
    //判断购物车商品已全选
    return this.data.cartGoods.every(function (element, index, array) {
      if (element.checked == true) {
        return true;
      } else {
        return false;
      }
    });
  },
  checkedItem: function (event) {
    let itemIndex = event.target.dataset.itemIndex;
    let that = this;

    if (!this.data.isEditCart) {
      util.request(api.CartChecked, { productIds: that.data.cartGoods[itemIndex].product_id, isChecked: that.data.cartGoods[itemIndex].checked ? 0 : 1 }, 'POST').then(function (res) {
        if (res.errno === 0) {
          that.setData({
            cartGoods: res.data.cartList,
            cartTotal: res.data.cartTotal
          });
        }

        that.setData({
          checkedAllStatus: that.isCheckedAll()
        });
      });
    } else {
      //编辑状态
      let tmpCartData = this.data.cartGoods.map(function (element, index, array) {
        if (index == itemIndex) {
          element.checked = !element.checked;
        }

        return element;
      });

      that.setData({
        cartGoods: tmpCartData,
        checkedAllStatus: that.isCheckedAll(),
        'cartTotal.checkedGoodsCount': that.getCheckedGoodsCount()
      });
    }
  },
  getCheckedGoodsCount: function () {
    let checkedGoodsCount = 0;
    this.data.cartGoods.forEach(function (v) {
      if (v.checked === true) {
        checkedGoodsCount += v.number;
      }
    });
    return checkedGoodsCount;
  },
  checkedAll: function () {
    let that = this;

    if (!this.data.isEditCart) {
      var productIds = this.data.cartGoods.map(function (v) {
        return v.product_id;
      });
      util.request(api.CartChecked, { productIds: productIds.join(','), isChecked: that.isCheckedAll() ? 0 : 1 }, 'POST').then(function (res) {
        if (res.errno === 0) {
          that.setData({
            cartGoods: res.data.cartList,
            cartTotal: res.data.cartTotal
          });
        }

        that.setData({
          checkedAllStatus: that.isCheckedAll()
        });
      });
    } else {
      //编辑状态
      let checkedAllStatus = that.isCheckedAll();
      let tmpCartData = this.data.cartGoods.map(function (v) {
        v.checked = !checkedAllStatus;
        return v;
      });

      that.setData({
        cartGoods: tmpCartData,
        checkedAllStatus: that.isCheckedAll(),
        'cartTotal.checkedGoodsCount': that.getCheckedGoodsCount()
      });
    }

  },
  editCart: function () {
    var that = this;
    if (this.data.isEditCart) {
      this.getCartList();
      this.setData({
        isEditCart: !this.data.isEditCart
      });
    } else {
      //编辑状态
      let tmpCartList = this.data.cartGoods.map(function (v) {
        v.checked = false;
        return v;
      });
      this.setData({
        editCartList: this.data.cartGoods,
        cartGoods: tmpCartList,
        isEditCart: !this.data.isEditCart,
        checkedAllStatus: that.isCheckedAll(),
        'cartTotal.checkedGoodsCount': that.getCheckedGoodsCount()
      });
    }

  },
  toIndexPage: function () {
    wx.switchTab({
      url: "/pages/index/index"
    });
  },
  updateCart: function (productId, goodsId, number, id) {
    let that = this;

    util.request(api.CartUpdate, {
      productId: productId,
      goodsId: goodsId,
      number: number,
      id: id
    }, 'POST').then(function (res) {
      if (res.errno === 0) {
        that.setData({
          //cartGoods: res.data.cartList,
          //cartTotal: res.data.cartTotal
        });
      }

      that.setData({
        checkedAllStatus: that.isCheckedAll()
      });
    });

  },
  cutNumber: function (event) {

    let itemIndex = event.target.dataset.itemIndex;
    let cartItem = this.data.cartGoods[itemIndex];
    let number = (cartItem.number - 1 > 1) ? cartItem.number - 1 : 1;
    cartItem.number = number;
    this.setData({
      cartGoods: this.data.cartGoods
    });
    this.updateCart(cartItem.product_id, cartItem.goods_id, number, cartItem.id);
  },
  addNumber: function (event) {
    let itemIndex = event.target.dataset.itemIndex;
    let cartItem = this.data.cartGoods[itemIndex];
    let number = cartItem.number + 1;
    cartItem.number = number;
    this.setData({
      cartGoods: this.data.cartGoods
    });
    this.updateCart(cartItem.product_id, cartItem.goods_id, number, cartItem.id);

  },
  checkoutOrder: function () {
    //获取已选择的商品
    let that = this;

    var checkedGoods = this.data.cartGoods.filter(function (element, index, array) {
      if (element.checked == true) {
        return true;
      } else {
        return false;
      }
    });

    if (checkedGoods.length <= 0) {
      return false;
    }


    wx.navigateTo({
      url: '../ ../shopping/checkout/checkout'
    })
  },
  deleteCart: function () {
    //获取已选择的商品
    let that = this;

    let productIds = this.data.cartGoods.filter(function (element, index, array) {
      if (element.checked == true) {
        return true;
      } else {
        return false;
      }
    });

    if (productIds.length <= 0) {
      return false;
    }

    productIds = productIds.map(function (element, index, array) {
      if (element.checked == true) {
        return element.product_id;
      }
    });


    util.request(api.CartDelete, {
      productIds: productIds.join(',')
    }, 'POST').then(function (res) {
      if (res.errno === 0) {
        let cartList = res.data.cartList.map(v => {
          v.checked = false;
          return v;
        });

        that.setData({
          cartGoods: cartList,
          cartTotal: res.data.cartTotal
        });
      }

      that.setData({
        checkedAllStatus: that.isCheckedAll()
      });
    });
  },
  // 购物车end

  // 我的收藏start
  getCollectList() {
    let that = this;
    util.request(api.CollectList, { typeId: that.data.typeId }).then(function (res) {
      if (res.errno === 0) {
        console.log(res.data);
        that.setData({
          collectList: res.data
        });
      }
    });
  },
  openGoods(event) {

    let that = this;
    let goodsId = this.data.collectList[event.currentTarget.dataset.index].value_id;
    console.log(goodsId);

    //触摸时间距离页面打开的毫秒数  
    var touchTime = that.data.touch_end - that.data.touch_start;
    console.log(touchTime);
    //如果按下时间大于350为长按  
    if (touchTime > 350) {
      wx.showModal({
        title: '',
        content: '确定删除收藏吗？',
        success: function (res) {
          if (res.confirm) {
            console.log('用户点击确认');
            util.request(api.CollectAddOrDelete, { typeId: that.data.typeId, valueId: goodsId }, 'POST').then(function (res) {
              if (res.errno === 0) {
                console.log(res.data);
                wx.showToast({
                  title: '删除成功',
                  icon: 'success',
                  duration: 2000
                });
                that.getCollectList();
              }
            });
          }
        }
      })
    } else {

      wx.navigateTo({
        url: '/pages/goods/goods?id=' + goodsId,
      });
    }
  },
  // 我的收藏end

  // 我的地址start
  getAddressList() {
    let that = this;
    util.request(api.AddressList).then(function (res) {
      if (res.errno === 0) {
        that.setData({
          addressList: res.data
        });
      }
    });
  },
  addressAddOrUpdate(event) {
    console.log(event)
    wx.navigateTo({
      url: '/pages/ucenter/addressAdd/addressAdd?id=' + event.currentTarget.dataset.addressId
    })
  },
  deleteAddress(event) {
    console.log(event.target)
    let that = this;
    wx.showModal({
      title: '',
      content: '确定要删除地址？',
      success: function (res) {
        if (res.confirm) {
          let addressId = event.target.dataset.addressId;
          util.request(api.AddressDelete, { id: addressId }, 'POST').then(function (res) {
            if (res.errno === 0) {
              that.getAddressList();
            }
          });
          console.log('用户点击确定')
        }
      }
    })
    return false;
  },
  // 我的地址end

  // 我的反馈
  bindPickerChange: function (e) {
    this.setData({
      fbindex: e.detail.value
    });
  },
  mobileInput: function (e) {
    let that = this;
    this.setData({
      fbmobile: e.detail.value,
    });
  },
  contentInput: function (e) {
    let that = this;
    this.setData({
      fbcontentLength: e.detail.cursor,
      fbcontent: e.detail.value,
    });
  },
  cleanMobile: function () {
    let that = this;
  },
  sbmitFeedback: function (e) {
    let that = this;
    if (that.data.fbindex == 0) {
      util.showErrorToast('请选择反馈类型');
      return false;
    }

    if (that.data.fbcontent == '') {
      util.showErrorToast('请输入反馈内容');
      return false;
    }

    if (that.data.fbmobile == '') {
      util.showErrorToast('请输入手机号码');
      return false;
    }
    wx.showLoading({
      title: '提交中...',
      mask: true,
      success: function () {
      }
    });

    util.request(api.FeedbackAdd, { mobile: that.data.fbmobile, index: that.data.fbindex, content: that.data.fbcontent }, 'POST').then(function (res) {
      if (res.errno === 0) {
        wx.hideLoading();
        wx.showToast({
          title: res.data,
          icon: 'success',
          duration: 2000,
          complete: function () {
            that.setData({
              fbindex: 0,
              fbcontent: '',
              fbcontentLength: 0,
              fbmobile: ''
            });
          }
        });
      } else {
        console.log(res);
        util.showErrorToast(res.data);
      }
    });
  }
  // 我的反馈end

})