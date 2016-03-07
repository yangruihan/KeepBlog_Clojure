(ns keepblog.views.user
  (:require [keepblog.views.config :as config]))

; 使用 selmer 模板库
(use 'selmer.parser)

; 设置参数
(config/set-config)

; 登录页面显示
(defn login []
  (render-file "login.html" {}))

; 注册页面显示
(defn register
  ([] (render-file "register.html" {}))
  ([user error] (render-file "register.html" {:user user,
                                              :error error})))

