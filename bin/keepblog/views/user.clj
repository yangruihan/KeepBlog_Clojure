(ns keepblog.views.user
  (:require [keepblog.views.config :as config]))

; 使用 selmer 模板库
(use 'selmer.parser)

; 设置参数
(config/set-config)

; 登录页面显示
(defn login 
  ([] (render-file "login.html" {}))
  ([info] (render-file "login.html" info)))

; 注册页面显示
(defn register
  ([] (render-file "register.html" {}))
  ([info] (render-file "register.html" info)))

