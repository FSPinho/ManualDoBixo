from django.contrib.auth.models import User, Group
from rest_framework.serializers import HyperlinkedModelSerializer

from api.models import Profile, Category, Article, Like


class UserSerializer(HyperlinkedModelSerializer):
    class Meta:
        model = User
        fields = ('url', 'username', 'first_name', 'last_name', 'email', 'groups')


class GroupSerializer(HyperlinkedModelSerializer):
    class Meta:
        model = Group
        fields = ('url', 'name')


class ProfileSerializer(HyperlinkedModelSerializer):
    class Meta:
        model = Profile
        fields = '__all__'


class CategorySerializer(HyperlinkedModelSerializer):
    class Meta:
        model = Category
        fields = '__all__'


class ArticleSerializer(HyperlinkedModelSerializer):
    class Meta:
        model = Article
        fields = '__all__'


class LikeSerializer(HyperlinkedModelSerializer):
    class Meta:
        model = Like
        fields = '__all__'