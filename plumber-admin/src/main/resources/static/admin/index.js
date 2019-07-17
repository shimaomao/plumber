layui.extend({
    admin: 'lay/modules/admin',
    validate: 'lay/modules/validate'
}).define(['admin', 'conf'], function (exports) {
    layui.admin.initPage();
    exports('index', {});
});