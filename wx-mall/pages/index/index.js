var util = require('../../utils/util.js');
var api = require('../../config/api.js');
var app = getApp();

// pages/indexv2/indexv2.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    suggestItems:[],
    suggestScenes:[],
    suggestGifts:[]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getSuggestItems();
    this.getSuggestScene();
    this.getSuggestGift();
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
  shopClick: function() {
    wx.navigateTo({
      url: '../shop/shop',
    })
  },
  getSuggestItems: function () {
    let that = this;
    util.request(api.SuggestItem, {scene:1}).then(function(res) {
      if (res.code == 0) {
        that.setData({
          suggestItems: res.items
        });
      }
    });
  },
  getSuggestScene: function() {
    let that = this;
    util.request(api.SuggestScene).then(function (res) {
      if (res.code == 0) {
        that.setData({
          suggestScenes: res.items
        });
      }
    });
  },
  getSuggestGift: function () {
    let that = this;
    util.request(api.SuggestItem, {scene:2}).then(function (res) {
      if (res.code == 0) {
        that.setData({
          suggestGifts: res.items
        });
      }
    });
  },
  clickMap: function() {
    wx.chooseLocation({success: function(res) {
      console.log(res);
    }});
  },
  itemClick: function(e) {
    wx.navigateTo({
      url: '../goods/goods?id=' + e.currentTarget.dataset.goodsid,
    });
  },
  sceneClick: function(e) {
    var index = e.currentTarget.dataset['index'];
    app.sceneId = index;
    wx.switchTab({
      url: '../catalog/catalog'
    });
  }
})