(ns keepblog.views.index
  (:require [keepblog.views.config :as config]))

; 使用 selmer 模板库
(use 'selmer.parser)
; 设置参数
(config/set-config)

(defn index 
  ([] (render-file "index.html" {}))
  ([info] (render-file "index.html" info)))
