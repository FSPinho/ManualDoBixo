from __future__ import unicode_literals

from django.db import models


class Category(models.Model):
    name = models.CharField(max_length=256)
    detail = models.CharField(max_length=1024)
