(ns keepblog.middleware.urlhandlers
  (:use [compojure.core :only (defroutes)])
  (:require [keepblog.controllers.index :as index]
            [keepblog.controllers.login :as login]))

(defroutes app-routes
  index/routes)

