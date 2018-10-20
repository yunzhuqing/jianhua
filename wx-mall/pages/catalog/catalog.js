var util = require('../../utils/util.js');
var api = require('../../config/api.js');
var app = getApp();

Page({
  data: {
    navList: [],
    subList: [],
    cols: 2,
    categoryList: [],
    currentCategory: {},
    scrollLeft: 0,
    scrollTop: 0,
    goodsCount: 0,
    scrollHeight: 0,
    toView: 0
  },
  onLoad: function (options) {
    this.getCatalog(options.sceneId);
  },
  getCatalog: function (index) {
    //CatalogList
    let that = this;
    wx.showLoading({
      title: '加载中...',
    });
    util.request(api.SceneSubList + "/0").then(function (res) {
        that.setData({
          navList: res.scenes
        });
        wx.hideLoading();
        if(index != null) {
          var category = { 'id': index };
          that.setData({
            currentCategory: category
          });
        } else {
          that.setData({
            currentCategory: res.current
          });
        }
        that.getList(that.data.currentCategory.id);
    });
  },
  getCurrentCategory: function (id) {
    let that = this;
    util.request(api.SceneList + "/" + id)
      .then(function (res) {
        that.setData({
          currentCategory: res.current
        });
      });
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  },
  getList: function (id) {
    var that = this;
    util.request(api.SceneSubList + "/" + id)
      .then(function (res) {        
        that.setData({
          subList: res.scenes
        });
      });
  },
  switchCate: function (event) {
    var that = this;
    var currentTarget = event.currentTarget;
    if (this.data.currentCategory.id == event.currentTarget.dataset.id) {
      return false;
    }
    that.getCurrentCategory(event.currentTarget.dataset.id);
    that.getList(event.currentTarget.dataset.id);
  },
  itemClick: function(e) {
    wx.navigateTo({
      url: '../goods/goods?id=' + e.currentTarget.dataset.goodsid,
    });
  }
})