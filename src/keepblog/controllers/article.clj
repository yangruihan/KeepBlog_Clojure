(ns keepblog.controllers.article
  (:require [compojure.core :refer [defroutes GET POST]]
            [noir.session :as session]
            [noir.validation :as vali]
            [noir.response :as resp]
            [clojure.string :as str]
            [keepblog.utils.collection :as col-utils]
            [keepblog.views.article :as article-view]
            [keepblog.models.category :as category]
            [keepblog.models.article :as article]
            [keepblog.models.tag :as tag]
            [keepblog.models.tag_article :as tag_article]))

(defn- create-article []
  (if-let [user-id (session/get :user-id)]
    (article-view/create-article
      {:categories (category/get-all-categories-by-user {:id user-id})})
    (article-view/create-article {})))

(defn- save-article [new-article]
  (let [{:keys [title category content]} new-article]
    (if-let [user-id (session/get :user-id)]
      (if-let [saved-article (article/create! {:user_id user-id,
                                               :title title,
                                               :category_id category,
                                               :content content})]
        saved-article
        nil)
      nil)))

(defn- save-tag [new-article article-id]
  (let [{:keys [tag]} new-article]
    (let [[tags saved-tag-articles]
          [(str/split tag #";") '()]]
      (let [tag-ids (tag/get-tag-ids-by-names tags)]
	      (if (or (col-utils/in? tag-ids nil) (nil? tag-ids))
	        (vali/set-error :get-tag-id-error "没有此标签")
	        (for [tag-id (tag/get-tag-ids-by-names tags)]
	          (if-let [saved-tag-article (tag_article/create! tag-id article-id)]
	            tag-id)))))))

(defn- create-article-action [new-article]
  (if-let [article-id (:id (save-article new-article))]
    (let [saved-tags (save-tag new-article article-id)]
	    (if (or (col-utils/in? saved-tags nil) (nil? saved-tags))
       (do
         (article/delete-by-id! article-id)
         (article-view/create-article {:article new-article,
		                                   :categories (category/get-all-categories-by-user (session/get :user-id)),
		                                   :error (first (vali/get-errors))}))
       (article-view/create-article-suc {:article (first (article/get-by {:id article-id}))})))))

(defroutes routes
  (GET "/create_article" [] (create-article))
  (POST "/create_article" {article :params} (create-article-action article)))
