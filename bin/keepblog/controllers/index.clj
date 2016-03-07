(ns keepblog.controllers.index
  (:require [compojure.core :refer [defroutes GET POST]]
            [keepblog.views.index :as index-view]))

;; 主页
(defn index []
  (index-view/index))

;; 路由设置
(defroutes routes
  (GET "/" [] (index)))

