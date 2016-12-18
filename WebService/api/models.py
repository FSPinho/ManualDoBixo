from __future__ import unicode_literals

from django.contrib.auth.models import User
from django.db import models
from imagekit.models.fields import ImageSpecField
from pilkit.processors.resize import ResizeToFill


class Profile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    image = models.ImageField(upload_to='images/profile', default=None)
    imageSmall = ImageSpecField(
        source='image',
        processors=[ResizeToFill(192, 192)],
        format='JPEG',
        options={'quality': 60}
    )
    imageMedium = ImageSpecField(
        source='image',
        processors=[ResizeToFill(1280, 1280 * 4 / 3)],
        format='JPEG',
        options={'quality': 60}
    )
    imageLarge = ImageSpecField(
        source='image',
        processors=[ResizeToFill(1920, 1920 * 4 / 3)],
        format='JPEG',
        options={'quality': 60}
    )

    def __str__(self):
        return self.user.get_full_name()


class Category(models.Model):
    name = models.CharField(max_length=256)
    detail = models.CharField(max_length=1024)

    def __str__(self):
        return self.name


class Article(models.Model):
    author = models.ForeignKey(User, default=None, on_delete=models.CASCADE, related_name='articles')
    category = models.ForeignKey(Category, default=None, on_delete=models.CASCADE, related_name='articles')
    title = models.CharField(max_length=256, default='')
    text = models.TextField(max_length=1024*1024*1024, default='')
    image = models.ImageField(upload_to='images/article', default=None)
    imageSmall = ImageSpecField(
        source='image',
        processors=[ResizeToFill(192, 192)],
        format='JPEG',
        options={'quality': 60}
    )
    imageMedium = ImageSpecField(
        source='image',
        processors=[ResizeToFill(1280, 1280 * 4/3)],
        format='JPEG',
        options={'quality': 60}
    )
    imageLarge = ImageSpecField(
        source='image',
        processors=[ResizeToFill(1920, 1920 * 4/3)],
        format='JPEG',
        options={'quality': 60}
    )

    def __str__(self):
        return self.title


class Like(models.Model):
    user = models.ForeignKey(User, default=None, on_delete=models.CASCADE, related_name='likes')
    article = models.ForeignKey(Article, default=None, on_delete=models.CASCADE, related_name='likes')
    timestamp = models.BigIntegerField(default=0)

    def __str__(self):
        return "%s at %s" % (self.user.get_full_name(), self.date)