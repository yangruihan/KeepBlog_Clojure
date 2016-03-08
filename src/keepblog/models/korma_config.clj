(ns keepblog.models.korma_config
  (:use [korma.db :refer [defdb mysql]]
        [korma.core :exclude [update]])
  (:require [clojure.string :as str]))

(defdb db
  (mysql {:db "keepblog",
          :host "localhost",
          :port 3306,
          :user "root",
          :password "123456"}))

(declare users user_infos tags tag_article categories articles)

(defentity users
  (has-one user_infos {:fk :user_id})
  (has-many categories {:fk :user_id})
  (has-many articles {:fk :user_id}))
    

(defentity user_infos
  (belongs-to users {:fk :user_id}))

(defentity tags
  (many-to-many articles :tag_article {:fk :tag_id}))

(defentity categories
  (belongs-to articles {:fk :category_id})
  (belongs-to users {:fk :user_id}))

(defentity articles
  (belongs-to users {:fk :user_id})
  (has-one categories {:fk :category_id})
  (many-to-many tags :tag_article {:fk :article_id}))

(defentity tag_article)

