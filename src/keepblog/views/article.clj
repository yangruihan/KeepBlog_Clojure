(ns keepblog.views.article
  (:require [keepblog.views.config :as config]))

; 使用 selmer 模板库
(use 'selmer.parser)
; 设置参数
(config/set-config)

(defn create-article 
  ([info] (render-file "create_article.html" info)))

(defn create-article-suc
  ([info] (render-file "create_article_suc.html" info)))
