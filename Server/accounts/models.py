from authemail.models import EmailUserManager, EmailAbstractUser
from django.template.loader import render_to_string
from django.conf import settings
from django.core.mail.message import EmailMultiAlternatives

class MyUser(EmailAbstractUser):
    # Required
    objects = EmailUserManager()


def send_multi_format_email(template_prefix, template_ctxt, target_email):
    subject_file = 'authemail/%s_subject.txt' % template_prefix
    txt_file = 'authemail/%s.txt' % template_prefix
    html_file = 'authemail/%s.html' % template_prefix

    subject = render_to_string(subject_file).strip()
    from_email = settings.EMAIL_FROM
    to = target_email
    bcc_email = settings.EMAIL_BCC
    text_content = render_to_string(txt_file, template_ctxt)
    html_content = render_to_string(html_file, template_ctxt)
    msg = EmailMultiAlternatives(subject, text_content, from_email, [to],
                                 bcc=[bcc_email])
    msg.attach_alternative(html_content, 'text/html')
    msg.send()