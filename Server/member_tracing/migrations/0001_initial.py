# Generated by Django 3.2.9 on 2021-11-24 11:43

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='MemberTracing',
            fields=[
                ('date', models.DateTimeField()),
                ('id', models.CharField(max_length=100, primary_key=True, serialize=False, unique=True)),
                ('latitude', models.FloatField(default='0', null=True)),
                ('longitude', models.FloatField(default='0', null=True)),
            ],
            options={
                'ordering': ['date'],
            },
        ),
    ]
