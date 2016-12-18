from django.contrib.auth.models import User, Group
from django.shortcuts import render
from rest_framework import permissions
from rest_framework.viewsets import ModelViewSet

from api.models import Profile, Category, Article, Like
from api.serializers import ProfileSerializer, UserSerializer, GroupSerializer, CategorySerializer, ArticleSerializer, \
    LikeSerializer


class UserViewSet(ModelViewSet):
    queryset = User.objects.all().order_by('-date_joined')
    serializer_class = UserSerializer


class GroupViewSet(ModelViewSet):
    queryset = Group.objects.all()
    serializer_class = GroupSerializer


class ProfileViewSet(ModelViewSet):
    queryset = Profile.objects.all()
    serializer_class = ProfileSerializer


class CategoryViewSet(ModelViewSet):
    queryset = Category.objects.all()
    serializer_class = CategorySerializer


class ArticleViewSet(ModelViewSet):
    queryset = Article.objects.all()
    serializer_class = ArticleSerializer


class LikeViewSet(ModelViewSet):
    queryset = Like.objects.all()
    serializer_class = LikeSerializer
