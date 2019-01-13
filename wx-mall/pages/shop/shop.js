var tabs = [
  {
    name: "热销商品"
  }
];

var util = require('../../utils/util.js');
var api = require('../../config/api.js');

// pages/shop/shop.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    tabs: tabs,     //展示的数据
    slideOffset: 0,//指示器每次移动的距离
    activeIndex: 0,//当前展示的Tab项索引
    sliderWidth: 96,//指示器的宽度,计算得到
    contentHeight: 0,//页面除去头部Tabbar后，内容区的总高度，计算得到，
    goodsArr:[],
    shopInfo:{}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          //计算相关宽度
          sliderWidth: res.windowWidth / that.data.tabs.length,
          sliderOffset: res.windowWidth / that.data.tabs.length * that.data.activeIndex,
          contentHeight: res.windowHeight
        });
      }
    });
    let shopId = options.id;
    util.request(api.GoodsShop, { "shopId": shopId, "page": 1, "limit": 10, "sidx":"id", "order":"desc"}).then(function (res) {
      if (res.code === 0) {
        that.setData({
          goodsArr: res.page.list
        });
      }
    });

    util.request(api.ShopInfo + '/' + shopId).then(function (res) {
      if (res.code === 0) {
        that.setData({
          shopInfo: res.shop
        });
      }
    });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
  
  bindChange: function (e) {
    var current = e.detail.current;
    this.setData({
      activeIndex: current,
      sliderOffset: this.data.sliderWidth * current
    });
  },

  navTabClick: function (e) {
    this.setData({
      sliderOffset: e.currentTarget.offsetLeft,
      activeIndex: e.currentTarget.id
    });
  },

  itemClick: function(e) {
    wx.navigateTo({
      url: '../goods/goods?id=1181000',
    })
  }
})
