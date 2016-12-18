from django.contrib import admin

from api.models import Article, Category, Like

admin.site.register(Category)
admin.site.register(Article)
admin.site.register(Like)