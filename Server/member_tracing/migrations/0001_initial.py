# Generated by Django 3.2.9 on 2021-11-27 08:19

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='MemberTracing',
            fields=[
                ('date', models.DateTimeField()),
                ('id', models.CharField(max_length=100, primary_key=True, serialize=False, unique=True)),
                ('latitude', models.FloatField(default='0', null=True)),
                ('longitude', models.FloatField(default='0', null=True)),
                ('owner', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='member_tracing', to=settings.AUTH_USER_MODEL)),
            ],
            options={
                'ordering': ['date'],
            },
        ),
    ]
