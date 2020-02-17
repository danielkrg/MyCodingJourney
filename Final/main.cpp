#include <windows.h>
#include <iostream>
#include <fstream>
#include <sstream> // needed for string stream
#include <vector>

#include <GL/glut.h>
#include <windows.h>

using namespace std;

int choice = 0;
float x = 0.0f;

int leftt = -100, rightt = 100, bottom = -100, top = 100;

void draw_points()
{
    glColor3f(0.0,0.0,0.0);
    glBegin(GL_POINTS);
        glVertex2f(0, 0);
        glVertex2f(0, 50);
        glVertex2f(50, 0);
        glVertex2f(-50, 50);
    glEnd();
    glFlush();
}

void draw_lines()
{
    glColor3f(0.0,0.0,0.0);
    glBegin(GL_LINE_STRIP);
        glVertex2f(0, 0);
        glVertex2f(0, 50);
        glVertex2f(50, 0);
        glVertex2f(-50, 50);
    glEnd();
    glFlush();
}

void draw_polygon()
{
    glColor3f(0.0,0.0,0.0);
    glBegin(GL_POLYGON);
        glVertex2f(-50, 0);
        glVertex2f(50, 0);
        glVertex2f(50, -50);
        glVertex2f(-50, -50);
    glEnd();
    glFlush();
}

void draw_solid()
{
    glColor3f(0, 0, 0);
    glPushMatrix();
        glTranslatef(0.0, 0.0, 0.0);
        glutSolidSphere(30, 10, 10);
    glPopMatrix();
}

void draw_transformations()
{
    x += 0.005;
    glColor3f(0.0,0.0,0.0);
    glBegin(GL_POLYGON);
        glVertex2f(-50 + x, 0);
        glVertex2f(50 + x, 0);
        glVertex2f(50 + x, -50);
        glVertex2f(-50 + x, -50);
    glEnd();
    glFlush();
}

void bitprint(string s) {
    for(int i = 0; i < s.length(); i++) {
        glutBitmapCharacter(GLUT_BITMAP_8_BY_13, s.at(i));
    }
}

void draw_text()
{
    glColor3f(0, 0, 0);
    glRasterPos3f(0.0, 0.0, 0.0);
    bitprint("OpenGL");
}

void mouse(int button, int state, int x, int y)
{
    if(state== GLUT_DOWN and button == GLUT_LEFT_BUTTON)
    {
    cout<<x<<" "<<y<<endl;
    }
}

void display()
{
    glClearColor(1.0,1.0,1.0,1.0); // white background
    glClear(GL_COLOR_BUFFER_BIT);

    glLoadIdentity();
    glOrtho(leftt, rightt, bottom, top, -1, 1);

    if (choice == 1)
    {
        draw_points();
    }
    else if (choice == 2)
    {
        draw_lines();
    }
    else if (choice == 3)
    {
        draw_polygon();
    }
    else if (choice == 4)
    {
        draw_solid();
    }
    else if (choice == 5)
    {
        draw_transformations();
    }
    else if (choice == 6)
    {
        draw_text();
    }
}

static void key(unsigned char key, int x, int y)
{
    switch (key)
    {
    case 27 :
    case 'q':
        exit(0);
        break;
    }
    glutPostRedisplay();
}

void init()
{
    cout<<"Input 1 for points"<<endl;
    cout<<"Input 2 for lines"<<endl;
    cout<<"Input 3 for polygons"<<endl;
    cout<<"Input 4 for solids"<<endl;
    cout<<"Input 5 for transformations"<<endl;
    cout<<"Input 6 for text"<<endl;
    cout<<"Click on screen for coordinates"<<endl;
    cout<<"Press q or exit to close program"<<endl;
    cout<<"Enter choice: "<<endl;
    cin>>choice;
}

int main( int argc, char **argv )
{
    glutInit(&argc, argv);
    init();
    glutInitDisplayMode(GLUT_RGB);
    glutInitWindowSize(600, 600);
    glutCreateWindow("Drawing");
    glutDisplayFunc(display);
    glutKeyboardFunc(key);
    glutMouseFunc(mouse);
    glutMainLoop();
    return 0;
}
