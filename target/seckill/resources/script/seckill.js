//存放主要交互逻辑js代码
//javascript 模块化
var seckill = {
    //封装秒杀相关ajax的url
    URL: {
        now: function () {
            return '/seckill/time/now'
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' +
                md5 + '/execute';

        }
    },
    //详情页秒杀逻辑
    //验证手机号
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },

    countdown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');

        if (nowTime > endTime) {
            //秒杀已结束
            seckillBox.html('秒杀已结束！');
        } else if (nowTime < startTime) {
            //秒杀未开始
            var killTime = new Date(startTime + 1000);//加1秒，用户端计时偏移的修正

            //这里的countDown是回调，不是很懂！！！
            seckillBox.countdown(killTime, function (event) {
                var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish countdown', function () {
                //倒计时结束后，获取秒杀地址，使用户可以进行秒杀的相关操作
                seckill.handleSeckill(seckillId, seckillBox);
            });
        } else {
            //秒杀开始
            seckill.handleSeckill(seckillId, seckillBox);
        }

    },

    //获取秒杀地址，控制显示逻辑，执行秒杀
    handleSeckill: function (seckillId, node) {
        node.hide()
            .html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');

        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    //开启秒杀
                    var md5 = exposer['md5'];
                    console.log('md5:' + md5);
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log('killUrl:' + killUrl);

                    //使用one不使用click，只绑定一次点击事件，防止用户重复秒杀
                    $('#killBtn').one('click', function () {
                        //执行秒杀请求
                        //1.禁用按钮
                        $(this).addClass('disabled');
                        //2.秒杀发送秒杀请求，执行秒杀
                        $.post(killUrl, {}, function (result) {
                            var success = result['success'];
                            console.log("success:" + success);
                            if (result && result['success']) {
                                var state;
                                var stateInfo;
                                var killResult;
                                var error = "手机号码未注册";
                                if(result['error'] == error){
                                    stateInfo = error;
                                }else{
                                    killResult = result['data'];
                                    state = killResult['state'];
                                    stateInfo = killResult['stateInfo'];
                                }
                                console.log("killResult:" + killResult);

                                console.log("stateInfo:" + stateInfo);
                                //3.显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        });
                    });
                    node.show();
                } else {
                    //客户端可能存在计时过快现象使得秒杀未开启
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];

                    //重新运行计时逻辑，更正客户端计时偏差
                    seckill.countdown(seckillId, now, start, end);
                }

            } else {
                console.log('result:' + result);
            }
        })
    },

    detail: {
        //详情页初始化
        //手机验证和交互，计时登录
        //规划交互流程
        //通过cookie查找手机号
        init: function (params) {
            var killPhone = $.cookie('killPhone');

            if (!seckill.validatePhone(killPhone)) {

                //获得弹出层窗体
                var killPhoneModal = $('#killPhoneModal');

                killPhoneModal.modal({
                    show: true,//显示弹出窗口
                    backdrop: 'static',//禁止位置关闭
                    keyboard: false//禁止键盘关闭
                });

                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {
                        //将手机号码写入cookie
                        //expire指cookie保存时间
                        //path指定相应web应用才携带cookie
                        $.cookie('killPhone', inputPhone, {expire: 7, path: '/seckill'});
                        //刷新页面
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html(
                            '<lable class="label label-danger">手机号码有误！</lable>').show('300')
                    }
                });
            }

            //用户登录成功
            //计时交互
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    var nowTime = result['data'];

                    console.log("nowTime= " + nowTime);
                    //时间判断
                    seckill.countdown(seckillId, nowTime, startTime, endTime);

                } else {
                    console.log("result=" + result);
                }
            });

        }
    }
}














